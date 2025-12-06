package controle.services;

import modelo.Tarefa;
import modelo.Usuario;
import interfaces.validators.IValidadorTarefa;
import interfaces.repositories.ITarefaRepository;
import interfaces.services.ITarefaService;
import repositorios.TarefaCacheRepository;
import builders.TarefaBuilder;
import strategies.FiltroPorDataStrategy;
import strategies.FiltroCriticasStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Service responsável pela lógica de negócio das tarefas.
 * <p>
 * Centraliza as operações de CRUD de tarefas, aplicando validações,
 * cache e padrões de projeto (Builder, Strategy, Observer).
 * </p>
 */
public class TarefaService implements ITarefaService {
    private ITarefaRepository repositorio;
    private IValidadorTarefa validador;
    private TarefaCacheRepository cacheRepository;
    
    // Implementação do padrão Observer
    private List<interfaces.observer.IObserver> observadores = new ArrayList<>();

    public TarefaService(ITarefaRepository repositorio, IValidadorTarefa validador, TarefaCacheRepository cacheRepository) {
        this.repositorio = repositorio;
        this.validador = validador;
        this.cacheRepository = cacheRepository;
    }

    @Override
    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario) {
        if (!validador.validarTitulo(titulo)) {
            return false;
        }
        try {
            // Criação usando Builder Pattern (HEAD)
            Tarefa novaTarefa = new TarefaBuilder()
                    .comTitulo(titulo)
                    .comDescricao(descricao)
                    .comPrazo(deadline)
                    .comPrioridade(prioridade)
                    .construir();
            
            // Vincula usuário (Remote)
            novaTarefa.setUsuario(usuario);

            // 1. Persistência Real (SQL)
            repositorio.salvar(novaTarefa);

            // 2. Atualiza Memória RAM (Otimização Remote)
            if (usuario.getTarefas() != null) {
                usuario.getTarefas().add(novaTarefa);
            }

            // 3. Invalida Redis (Remote)
            cacheRepository.invalidarCache(usuario.getEmail());
            System.out.println("[SYNC] Tarefa criada. Redis invalidado e memória atualizada.");

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editar(String tituloAntigo, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade, double novoPercentual) {
        if (!validador.validarTitulo(novoTitulo)) {
            return false;
        }
        try {
            Tarefa tarefaOriginal = buscarPorTitulo(tituloAntigo);
            if (tarefaOriginal == null) {
                return false;
            }

            // Uso do Builder para criar a versão atualizada
            Tarefa tarefaAtualizada = new TarefaBuilder()
                    .comTitulo(novoTitulo)
                    .comDescricao(novaDescricao)
                    .comDataCadastro(tarefaOriginal.getDataCadastro())
                    .comPrazo(novoDeadline)
                    .comPrioridade(novaPrioridade)
                    .comPercentual(novoPercentual)
                    .construir();
            
            // Preserva ID e Usuario
            tarefaAtualizada.setId(tarefaOriginal.getId());
            tarefaAtualizada.setUsuario(tarefaOriginal.getUsuario());

            // 1. Atualiza SQL
            repositorio.atualizar(tarefaOriginal, tarefaAtualizada);

            // 2. Atualiza Memória RAM
            Usuario usuario = tarefaOriginal.getUsuario();
            if (usuario != null && usuario.getTarefas() != null) {
                List<Tarefa> listaMemoria = usuario.getTarefas();
                for(int i=0; i<listaMemoria.size(); i++) {
                    if(listaMemoria.get(i).getId().equals(tarefaOriginal.getId())) {
                        listaMemoria.set(i, tarefaAtualizada);
                        break;
                    }
                }
                // 3. Invalida Redis
                cacheRepository.invalidarCache(usuario.getEmail());
            }

            return true;
        } catch (Exception erro) {
            erro.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean excluir(String titulo) {
        try {
            Tarefa tarefa = buscarPorTitulo(titulo);
            if (tarefa != null) {
                // 1. Remove SQL
                repositorio.remover(tarefa);
                
                // 2. Remove Memória RAM
                Usuario usuario = tarefa.getUsuario();
                if (usuario != null && usuario.getTarefas() != null) {
                    usuario.getTarefas().removeIf(t -> t.getId().equals(tarefa.getId()));
                    // 3. Invalida Redis
                    cacheRepository.invalidarCache(usuario.getEmail());
                }
                
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Tarefa buscarPorTitulo(String titulo) {
        return repositorio.buscarPorTitulo(titulo);
    }

    // --- MÉTODOS OTIMIZADOS (Redis/Memória) ---

    @Override
    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        if (usuario == null) return Collections.emptyList();
        System.out.println("[PERFORMANCE] Listando tarefas da memória RAM do objeto Usuario.");
        return usuario.getTarefas();
    }

    @Override
    public List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario) {
        if (usuario == null) return Collections.emptyList();
        return usuario.getTarefas().stream()
                .filter(t -> t.getDeadline().equals(data))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarefa> listarCriticasPorUsuario(Usuario usuario) {
        if (usuario == null) return Collections.emptyList();
        return usuario.getTarefas().stream()
                .filter(Tarefa::isCritica)
                .collect(Collectors.toList());
    }

    // --- MÉTODOS STRATEGY (Genéricos) ---

    @Override
    public List<Tarefa> listar(interfaces.strategies.IFiltroStrategy filtro) {
        // Por padrão, usa listarTodas (vai no banco), mas o ideal seria passar o usuario contexto
        // Como o método é genérico, mantemos comportamento original
        return filtro.filtrar(listarTodas());
    }

    @Override
    public List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy estrategia) {
        return estrategia.ordenar(listarTodas());
    }

    // --- MÉTODOS OBSERVER ---

    @Override
    public void adicionarObservador(interfaces.observer.IObserver observer) {
        observadores.add(observer);
    }

    @Override
    public void removerObservador(interfaces.observer.IObserver observer) {
        observadores.remove(observer);
    }

    @Override
    public void notificarObservadores(Object mensagem) {
        for (interfaces.observer.IObserver observer : observadores) {
            observer.atualizar(mensagem);
        }
    }

    // --- MÉTODOS LEGADOS/AUXILIARES ---

    @Override
    public List<Tarefa> listarTodas() {
        return repositorio.listarTodas();
    }

    @Override
    public List<Tarefa> listarPorData(LocalDate data) {
        return listar(new FiltroPorDataStrategy(data));
    }

    @Override
    public List<Tarefa> listarCriticas() {
        return listar(new FiltroCriticasStrategy());
    }

    @Override
    public void atualizarPercentual(Long idTarefa, double novoPercentual) {
        try {
            Tarefa tarefa = repositorio.buscarPorId(idTarefa);
            if (tarefa != null) {
                tarefa.setPercentual(novoPercentual);
                repositorio.salvar(tarefa);
                notificarObservadores("Tarefa atualizada: " + tarefa.getTitulo());
                
                // Atualiza cache se possível
                if (tarefa.getUsuario() != null) {
                    cacheRepository.invalidarCache(tarefa.getUsuario().getEmail());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}













    
    
    

    
            
        
        
        
    

    
    
        
            
        
            
            

            
            

            
            

            
            
            

            
        
            
            
        
    

    
    
            
        
            
        
            
            
                

            
                    
            
            
            

            
            

            
            
                 
                 
                    
                    
                
            

            
            

            
        
            
        
    

    
    
        
            
            
                
                

                
                

                
                

                
            
            
        
            
        
    

    
    
        
        
    

    

    
    
        
            

        
        
        
        
        
    

    
    
        
            
        
        
                
                
    

    
    
        
            
        
        
                
                
    

    
    
    
        
    

    
    
        
    

    
    
        
    

    
    // 
    
    
    

    
    
    

    
    
    

    