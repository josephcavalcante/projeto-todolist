package controle.services;

import modelo.Tarefa;
import modelo.Usuario;
import interfaces.validators.IValidadorTarefa;
import interfaces.repositories.ITarefaRepository;
import interfaces.services.ITarefaService;
import repositorios.TarefaCacheRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

public class TarefaService implements ITarefaService {
    private ITarefaRepository repositorio;
    private IValidadorTarefa validador;
    private TarefaCacheRepository cacheRepository;

    public TarefaService(ITarefaRepository repositorio, IValidadorTarefa validador, TarefaCacheRepository cacheRepository) {
        this.repositorio = repositorio;
        this.validador = validador;
        this.cacheRepository = cacheRepository;
    }

    @Override
    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario) {
        if (!validador.validarTitulo(titulo)) return false;
        try {
            Tarefa novaTarefa = new Tarefa(titulo.trim(), descricao.trim(), LocalDate.now(), deadline, prioridade);
            novaTarefa.setUsuario(usuario);
            
            // 1. Persistência Real (SQL)
            repositorio.salvar(novaTarefa);
            
            // 2. Atualiza Memória RAM (para a tela ver imediatamente)
            usuario.getTarefas().add(novaTarefa);
            
            // 3. Invalida Redis (para forçar recarregamento no próximo login)
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
        if (!validador.validarTitulo(novoTitulo)) return false;
        try {
            Tarefa tarefaOriginal = buscarPorTitulo(tituloAntigo);
            if (tarefaOriginal == null) return false;

            Tarefa tarefaAtualizada = new Tarefa(novoTitulo.trim(), novaDescricao.trim(),
                    tarefaOriginal.getDataCadastro(), novoDeadline, novaPrioridade);
            tarefaAtualizada.setId(tarefaOriginal.getId());
            tarefaAtualizada.setPercentual(novoPercentual);
            tarefaAtualizada.setUsuario(tarefaOriginal.getUsuario());

            // 1. Atualiza SQL
            repositorio.atualizar(tarefaOriginal, tarefaAtualizada);
            
            // 2. Atualiza Memória RAM
            List<Tarefa> listaMemoria = tarefaOriginal.getUsuario().getTarefas();
            for(int i=0; i<listaMemoria.size(); i++) {
                if(listaMemoria.get(i).getId().equals(tarefaOriginal.getId())) {
                    listaMemoria.set(i, tarefaAtualizada);
                    break;
                }
            }

            // 3. Invalida Redis
            cacheRepository.invalidarCache(tarefaOriginal.getUsuario().getEmail());
            
            return true;
        } catch (Exception erro) {
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
                tarefa.getUsuario().getTarefas().removeIf(t -> t.getId().equals(tarefa.getId()));
                
                // 3. Invalida Redis
                cacheRepository.invalidarCache(tarefa.getUsuario().getEmail());
                
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Tarefa buscarPorTitulo(String titulo) {
        // Busca pontual vai no banco para garantir consistência
        return repositorio.buscarPorTitulo(titulo);
    }

    // --- MÉTODOS OTIMIZADOS (LEITURA DE MEMÓRIA) ---

    @Override
    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        if (usuario == null) return Collections.emptyList();
        
        // AQUI ESTÁ A CORREÇÃO:
        // Retorna a lista que já está no objeto Usuario (carregada no Login via Redis)
        // NÃO chama repositorio.listarPorUsuario(usuario)
        System.out.println("[PERFORMANCE] Listando tarefas da memória RAM do objeto Usuario.");
        return usuario.getTarefas();
    }

    @Override
    public List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario) {
        if (usuario == null) return Collections.emptyList();
        // Filtra na memória
        return usuario.getTarefas().stream()
                .filter(t -> t.getDeadline().equals(data))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarefa> listarCriticasPorUsuario(Usuario usuario) {
        if (usuario == null) return Collections.emptyList();
        // Filtra na memória
        return usuario.getTarefas().stream()
                .filter(Tarefa::isCritica)
                .collect(Collectors.toList());
    }

    // Métodos legados (não devem ser usados pelo Facade principal)
    @Override public List<Tarefa> listarTodas() { return Collections.emptyList(); }
    @Override public List<Tarefa> listarPorData(LocalDate d) { return Collections.emptyList(); }
    @Override public List<Tarefa> listarCriticas() { return Collections.emptyList(); }
    
    // Métodos Stub da Interface (podem ser ignorados ou removidos se limpar a interface)
    @Override public void adicionarObservador(interfaces.observer.IObserver o) {}
    @Override public void removerObservador(interfaces.observer.IObserver o) {}
    @Override public void notificarObservadores(Object m) {}
    @Override public List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy s) { return null; }
}