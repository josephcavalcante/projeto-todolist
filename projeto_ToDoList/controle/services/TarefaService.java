package controle.services;

import modelo.Tarefa;
import interfaces.IValidadorTarefa;
import interfaces.ITarefaRepository;
import validadores.ValidadorTarefa;
import repositorios.TarefaRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Service responsável pela lógica de negócio das tarefas.
 * <p>
 * Centraliza as operações de CRUD de tarefas, aplicando validações e 
 * coordenando com o repositório de dados. Segue o padrão Service Layer.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 1.1
 */
public class TarefaService {
    private ITarefaRepository repositorio;
    private IValidadorTarefa validador;

    /**
     * Construtor com injeção de dependência (DIP).
     * 
     * @param repositorio implementação do repositório de tarefas
     * @param validador implementação do validador de tarefas
     */
    public TarefaService(ITarefaRepository repositorio, IValidadorTarefa validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }
    
    /**
     * Construtor de compatibilidade com versão anterior.
     * 
     * @param manipulador manipulador de tarefas legado
     * @deprecated Use o construtor com injeção de dependência
     */
    @Deprecated
    public TarefaService(ManipuladorDeTarefas manipulador) {
        this.repositorio = new TarefaRepository(manipulador);
        this.validador = new ValidadorTarefa();
    }

    /**
     * Cadastra uma nova tarefa no sistema.
     * <p>
     * Valida os dados de entrada e, se válidos, cria e salva a tarefa.
     * A data de cadastro é definida automaticamente como a data atual.
     * </p>
     * 
     * @param titulo título da tarefa (obrigatório)
     * @param descricao descrição detalhada da tarefa
     * @param deadline data limite para conclusão
     * @param prioridade nível de prioridade (1-5)
     * @return true se a tarefa foi cadastrada com sucesso, false caso contrário
     */
    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade) {
        // validação usando validador dedicado
        if(!validador.validarTitulo(titulo)) {
            return false;
        }
        try {
            // instanciação da tarefa com data atual
            Tarefa novaTarefa = new Tarefa(titulo.trim(), descricao.trim(), LocalDate.now(), deadline, prioridade);
            repositorio.salvar(novaTarefa);
            return true; // operação bem-sucedida
        } catch (Exception ex) {
            return false; // falha na operação
        }
    }

    /**
     * Edita uma tarefa existente.
     * 
     * @param tituloAntigo título atual da tarefa a ser editada
     * @param novoTitulo novo título da tarefa
     * @param novaDescricao nova descrição da tarefa
     * @param novoDeadline nova data limite
     * @param novaPrioridade nova prioridade
     * @param novoPercentual novo percentual de conclusão
     * @return true se a edição foi bem-sucedida, false caso contrário
     */
    public boolean editar(String tituloAntigo, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade, double novoPercentual) {
        // verificação do novo título
        if (novoTitulo == null || novoTitulo.trim().length() == 0) {
            return false;
        }
        try {
            // localização da tarefa original
            Tarefa tarefaVelha = buscarPorTitulo(tituloAntigo);
            if (tarefaVelha == null) {
                return false; // tarefa inexistente
            }

            // construção da tarefa atualizada
            Tarefa tarefaEditada = new Tarefa(novoTitulo.trim(), novaDescricao.trim(), tarefaVelha.getDataCadastro(), novoDeadline, novaPrioridade);
            tarefaEditada.setPercentual(novoPercentual); // definição do percentual
            repositorio.atualizar(tarefaVelha, tarefaEditada);
            return true;
        } catch (Exception erro) {
            return false; // falha na edição
        }
    }

    /**
     * Exclui uma tarefa do sistema.
     * 
     * @param titulo título da tarefa a ser excluída
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluir(String titulo) {
        try {
            Tarefa tarefaParaRemover = buscarPorTitulo(titulo);
            if (tarefaParaRemover != null) {
                repositorio.remover(tarefaParaRemover);
                return true; // exclusão realizada
            }
            return false; // tarefa não localizada
        } catch (Exception ex) {
            return false; // falha na exclusão
        }
    }

    /**
     * Busca uma tarefa pelo título.
     * 
     * @param titulo título da tarefa a ser buscada
     * @return a tarefa encontrada ou null se não existir
     */
    public Tarefa buscarPorTitulo(String titulo) {
        return repositorio.buscarPorTitulo(titulo);
    }
}