package controle.services;

import modelo.Tarefa;
import modelo.Usuario;
import interfaces.validators.IValidadorTarefa;
import interfaces.repositories.ITarefaRepository;
import interfaces.services.ITarefaService;
import builders.TarefaBuilder;
import strategies.FiltroPorDataStrategy;
import strategies.FiltroCriticasStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Service responsável pela lógica de negócio das tarefas.
 * <p>
 * Refatorado para usar o padrão Proxy: Não gerencia mais cache explicitamente.
 * Apenas delega ao repositório e aplica regras de negócio (validação e
 * filtros).
 * </p>
 */
public class TarefaService implements ITarefaService {

    // O Repositório aqui será, em tempo de execução, o TarefaRepositoryProxy
    private ITarefaRepository repositorio;
    private IValidadorTarefa validador;

    // Removido: private TarefaCacheRepository cacheRepository;
    // O Service não precisa mais conhecer o cache!

    private List<interfaces.observer.IObserver> observadores = new ArrayList<>();

    public TarefaService(ITarefaRepository repositorio, IValidadorTarefa validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }

    // Removido: private void sincronizarCache(Usuario usuario) {...}
    // A invalidação agora é responsabilidade do Proxy ao chamar salvar/remover.

    @Override
    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario) {
        // 1. Validação preliminar
        if (!validador.validarTitulo(titulo)) {
            return false;
        }
        try {
            // 2. Construção do Objeto
            Tarefa novaTarefa = new TarefaBuilder()
                    .comTitulo(titulo)
                    .comDescricao(descricao)
                    .comPrazo(deadline)
                    .comPrioridade(prioridade)
                    .construir();

            novaTarefa.setUsuario(usuario);

            // 3. Validação Completa (Regras de Negócio)
            if (!validador.validarTarefa(novaTarefa)) {
                return false;
            }

            // 4. Persistência (Proxy) O Proxy intercepta isso, salva no SQL e invalida o
            // cache do usuário automaticamente
            repositorio.salvar(novaTarefa);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editar(String tituloAntigo, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade, double novoPercentual, Usuario usuario) {
        if (!validador.validarTitulo(novoTitulo)) {
            return false;
        }
        try {
            // Agora garantimos que só busca se for do usuário
            Tarefa tarefaOriginal = buscarPorTitulo(tituloAntigo, usuario);
            if (tarefaOriginal == null)
                return false;

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

            // Validação Completa antes de atualizar
            if (!validador.validarTarefa(tarefaAtualizada)) {
                return false;
            }
            // Chama o repositório. O Proxy vai atualizar o SQL e limpar o Cache.
            repositorio.atualizar(tarefaOriginal, tarefaAtualizada);

            return true;
        } catch (Exception erro) {
            erro.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean excluir(String titulo, Usuario usuario) {
        try {
            // Busca segura
            Tarefa tarefa = buscarPorTitulo(titulo, usuario);
            if (tarefa != null) {
                // O Proxy intercepta, remove do SQL e invalida o cache
                repositorio.remover(tarefa);
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // --- MÉTODOS DE LEITURA (Refatorados para usar Proxy) ---

    @Override
    public List<Tarefa> listar(interfaces.strategies.IFiltroStrategy filtro, Usuario usuario) {
        if (usuario == null)
            return new ArrayList<>();

        // 1. Delega a busca de dados ao Repositório (que é o Proxy)
        // O Proxy decide transparente se retorna a lista do Redis (rápido) ou do SQL
        List<Tarefa> todasTarefas = repositorio.listarPorUsuario(usuario);

        if (todasTarefas == null) {
            todasTarefas = new ArrayList<>();
        }

        // 2. Aplica o filtro (Strategy) em memória sobre os dados retornados
        // Isso mantém a lógica de negócio (filtragem) no Service, e a infra (cache) no
        // Proxy
        return filtro.filtrar(todasTarefas);
    }

    @Override
    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        // Usa uma estratégia "dummy" que retorna tudo, reaproveitando a lógica acima
        return listar(t -> t, usuario);
    }

    @Override
    public List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy estrategia, Usuario usuario) {
        // Busca tudo (via Proxy)
        List<Tarefa> tarefas = listar(t -> t, usuario);
        // Ordena em memória
        return estrategia.ordenar(tarefas);
    }

    // --- MÉTODOS OBSERVER E AUXILIARES ---

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

    @Override
    public void atualizarPercentual(Long idTarefa, double novoPercentual) {
        try {
            Tarefa tarefa = repositorio.buscarPorId(idTarefa);
            if (tarefa != null) {
                tarefa.setPercentual(novoPercentual);
                // O Proxy cuidará da consistência do cache aqui também
                repositorio.salvar(tarefa);
                notificarObservadores("Tarefa atualizada: " + tarefa.getTitulo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tarefa buscarPorTitulo(String titulo, Usuario usuario) {
        Tarefa t = repositorio.buscarPorTitulo(titulo);
        // Validação de Segurança: Se a tarefa não for do usuário, retorna null
        // (fingimos que não existe)
        if (t != null && t.getUsuario() != null && usuario != null) {
            if (!t.getUsuario().getEmail().equals(usuario.getEmail())) {
                return null;
            }
        }
        return t;
    }
}