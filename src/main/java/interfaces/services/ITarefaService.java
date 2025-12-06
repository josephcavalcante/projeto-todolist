package interfaces.services;

import java.time.LocalDate;
import java.util.List;

import modelo.Tarefa;
import modelo.Usuario;
import interfaces.observer.ISubject;

/**
 * Interface para serviços de gestão de tarefas.
 * <p>
 * Define operações CRUD para tarefas, mantendo baixo acoplamento
 * e seguindo o princípio ISP (Interface Segregation Principle).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.2
 * @since 1.0
 */
public interface ITarefaService extends ISubject {

    /**
     * Cadastra uma nova tarefa no sistema.
     * 
     * @param titulo     título da tarefa (obrigatório)
     * @param descricao  descrição detalhada da tarefa
     * @param deadline   data limite para conclusão
     * @param prioridade nível de prioridade (1-5)
     * @param usuario    usuário dono da tarefa
     * @return true se a tarefa foi cadastrada com sucesso, false caso contrário
     */
    boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario);

    /**
     * Edita uma tarefa existente.
     * 
     * @param tituloAntigo   título atual da tarefa a ser editada
     * @param novoTitulo     novo título da tarefa
     * @param novaDescricao  nova descrição da tarefa
     * @param novoDeadline   nova data limite
     * @param novaPrioridade nova prioridade
     * @param novoPercentual novo percentual de conclusão
     * @return true se a edição foi bem-sucedida, false caso contrário
     */
    boolean editar(String tituloAntigo, String novoTitulo, String novaDescricao,
            LocalDate novoDeadline, int novaPrioridade, double novoPercentual);

    /**
     * Exclui uma tarefa do sistema.
     * 
     * @param titulo título da tarefa a ser excluída
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    boolean excluir(String titulo);

    /**
     * Busca uma tarefa pelo título.
     * 
     * @param titulo título da tarefa a ser buscada
     * @return a tarefa encontrada ou null se não existir
     */
    Tarefa buscarPorTitulo(String titulo);

    /**
     * Lista todas as tarefas do sistema (Geral).
     * 
     * @return lista com todas as tarefas
     */
    List<Tarefa> listarTodas();

    // Métodos otimizados para Cache/Usuario
    List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario);

    List<Tarefa> listarPorUsuario(Usuario usuario);

    List<Tarefa> listarCriticasPorUsuario(Usuario usuario);

    // Métodos baseados em Strategy (Geral)
    List<Tarefa> listar(interfaces.strategies.IFiltroStrategy filtro);

    List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy estrategia);

    // Métodos Legados / Compatibilidade
    List<Tarefa> listarPorData(LocalDate data);

    List<Tarefa> listarCriticas();

    void atualizarPercentual(Long idTarefa, double novoPercentual);
}