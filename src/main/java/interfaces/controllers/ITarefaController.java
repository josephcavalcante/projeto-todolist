package interfaces.controllers;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para controller de tarefas.
 * <p>
 * Define operações de coordenação para gestão de tarefas,
 * seguindo o padrão GRASP Controller e o princípio DIP.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public interface ITarefaController {
    
    /**
     * Coordena a adição de uma nova tarefa.
     * 
     * @param titulo título da tarefa
     * @param descricao descrição detalhada
     * @param deadline prazo limite
     * @param prioridade nível de prioridade (1-5)
     * @return true se adicionada com sucesso
     */
    boolean adicionarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade);
    
    /**
     * Coordena a edição de uma tarefa existente.
     * 
     * @param tituloAntigo título atual da tarefa
     * @param novoTitulo novo título
     * @param novaDescricao nova descrição
     * @param novoDeadline novo prazo
     * @param novaPrioridade nova prioridade
     * @param novoPercentual novo percentual de conclusão
     * @return true se editada com sucesso
     */
    boolean editarTarefa(String tituloAntigo, String novoTitulo, String novaDescricao, 
                        LocalDate novoDeadline, int novaPrioridade, double novoPercentual);
    
    /**
     * Coordena a remoção de uma tarefa.
     * 
     * @param titulo título da tarefa a ser removida
     * @return true se removida com sucesso
     */
    boolean removerTarefa(String titulo);
    
    /**
     * Coordena a busca de uma tarefa por título.
     * 
     * @param titulo título da tarefa procurada
     * @return tarefa encontrada ou null
     */
    Tarefa buscarTarefa(String titulo);
    
    /**
     * Coordena a listagem de todas as tarefas.
     * 
     * @return lista com todas as tarefas
     */
    List<Tarefa> listarTodasTarefas();
    
    /**
     * Coordena a listagem de tarefas por data específica.
     * 
     * @param data data para filtrar as tarefas
     * @return lista de tarefas da data especificada
     */
    List<Tarefa> listarTarefasPorData(LocalDate data);
    
    /**
     * Coordena a listagem de tarefas críticas (prazo vencendo).
     * 
     * @return lista de tarefas com prazo crítico
     */
    List<Tarefa> listarTarefasCriticas();
}