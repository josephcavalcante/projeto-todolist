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

    public TarefaService(ITarefaRepository repositorio, IValidadorTarefa validador,
            TarefaCacheRepository cacheRepository) {
        this.repositorio = repositorio;
        this.validador = validador;
        this.cacheRepository = cacheRepository;
    }

    /**
     * Método auxiliar para centralizar a atualização do Cache.
     * Garante consistência entre Banco SQL e Redis.
     */
    private void sincronizarCache(Usuario usuario) {
        if (usuario == null)
            return;

        // 1. Invalida versão antiga (opcional, pois o salvar sobrescreve, mas é boa
        // prática)
        cacheRepository.invalidarCache(usuario.getId());

        // 2. Busca a versão mais recente do Banco de Dados (Fonte da verdade)
        List<Tarefa> tarefasAtualizadas = repositorio.listarPorUsuario(usuario);

        // 3. Salva o novo estado no Redis
        cacheRepository.salvarCache(usuario.getId(), tarefasAtualizadas);

        // 4. Atualiza a referência em memória do objeto Usuário para refletir tudo
        usuario.setTarefas(tarefasAtualizadas);

        System.out.println("[SYNC] Cache do Redis atualizado com sucesso para usuário: " + usuario.getEmail());
    }

    @Override
    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario) {
        if (!validador.validarTitulo(titulo)) {
            return false;
        }
        try {
            // Criação usando Builder Pattern
            Tarefa novaTarefa = new TarefaBuilder()
                    .comTitulo(titulo)
                    .comDescricao(descricao)
                    .comPrazo(deadline)
                    .comPrioridade(prioridade)
                    .construir();

            novaTarefa.setUsuario(usuario);

            // 1. Persistência Real (SQL)
            repositorio.salvar(novaTarefa);

            // 2. Atualização Proativa do Cache (Write-Through)
            sincronizarCache(usuario);

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

            tarefaAtualizada.setId(tarefaOriginal.getId());
            tarefaAtualizada.setUsuario(tarefaOriginal.getUsuario());

            // 1. Atualiza SQL
            repositorio.atualizar(tarefaOriginal, tarefaAtualizada);

            // 2. Atualização Proativa do Cache
            sincronizarCache(tarefaOriginal.getUsuario());

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
                Usuario usuario = tarefa.getUsuario();

                // 1. Remove SQL
                repositorio.remover(tarefa);

                // 2. Atualização Proativa do Cache
                // Mesmo ao excluir, recarregamos a lista (agora menor) e salvamos no Redis
                if (usuario != null) {
                    sincronizarCache(usuario);
                }

                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
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

    // --- MÉTODOS AUXILIARES ---

    @Override
    public void atualizarPercentual(Long idTarefa, double novoPercentual) {
        try {
            Tarefa tarefa = repositorio.buscarPorId(idTarefa);
            if (tarefa != null) {
                tarefa.setPercentual(novoPercentual);
                repositorio.salvar(tarefa);
                notificarObservadores("Tarefa atualizada: " + tarefa.getTitulo());

                // Atualiza cache também aqui
                if (tarefa.getUsuario() != null) {
                    sincronizarCache(tarefa.getUsuario());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tarefa buscarPorTitulo(String titulo) {
        return repositorio.buscarPorTitulo(titulo);
    }

    // --- MÉTODOS DE LEITURA (STRATEGY) ---

    @Override
    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        return listar(t -> t, usuario);
    }

    @Override
    public List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario) {
        return listar(new FiltroPorDataStrategy(data), usuario);
    }

    @Override
    public List<Tarefa> listarCriticasPorUsuario(Usuario usuario) {
        return listar(new FiltroCriticasStrategy(), usuario);
    }

    @Override
    public List<Tarefa> listar(interfaces.strategies.IFiltroStrategy filtro, Usuario usuario) {
        if (usuario == null)
            return new ArrayList<>();

        List<Tarefa> fonteDados;

        // 1. Tenta buscar do Cache (Redis)
        System.out.println("[CACHE] Buscando tarefas no Redis para: " + usuario.getEmail());
        fonteDados = cacheRepository.buscarCache(usuario.getId());

        if (fonteDados == null) {
            // 2. Cache Miss -> Busca do Banco SQL
            System.out.println("[CACHE] Miss. Buscando do SQL...");
            fonteDados = repositorio.listarPorUsuario(usuario);

            // 3. Salva no Cache para a próxima leitura (Cache-Aside)
            // Isso garante que se o cache expirar sozinho, ele será repopulado na leitura
            if (fonteDados != null) {
                cacheRepository.salvarCache(usuario.getId(), fonteDados);
            }
        } else {
            // Cache Hit
            usuario.setTarefas(fonteDados);
        }

        if (fonteDados == null) {
            fonteDados = new ArrayList<>();
        }

        return filtro.filtrar(fonteDados);
    }

    @Override
    public List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy estrategia, Usuario usuario) {
        List<Tarefa> tarefas = listar(t -> t, usuario);
        return estrategia.ordenar(tarefas);
    }
}
