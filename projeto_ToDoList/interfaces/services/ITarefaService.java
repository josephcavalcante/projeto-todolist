package interfaces.services;

import java.time.LocalDate;
import java.util.List;

import modelo.Tarefa;

/**
 * Interface para serviços de gestão de tarefas.
 * <p>
 * Define operações CRUD para tarefas, mantendo baixo acoplamento
 * e seguindo o princípio ISP (Interface Segregation Principle).
 * Permite diferentes implementações sem afetar o código cliente.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public interface ITarefaService {
    
    /**
     * Cadastra uma nova tarefa no sistema.
     * 
     * @param titulo título da tarefa (obrigatório)
     * @param descricao descrição detalhada da tarefa
     * @param deadline data limite para conclusão
     * @param prioridade nível de prioridade (1-5)
     * @return true se a tarefa foi cadastrada com sucesso, false caso contrário
     */
    boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade);
    
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
     * Lista todas as tarefas do sistema.
     * 
     * @return lista com todas as tarefas, ou lista vazia se não houver tarefas
     */
    List<Tarefa> listarTodas();
    
    /**
     * Lista tarefas filtradas por data específica.
     * 
     * @param data a data para filtrar as tarefas
     * @return lista de tarefas da data especificada
     */
    List<Tarefa> listarPorData(LocalDate data);
    
    /**
     * Lista tarefas críticas (prazo vencendo).
     * 
     * @return lista de tarefas críticas
     */
    List<Tarefa> listarCriticas();
}