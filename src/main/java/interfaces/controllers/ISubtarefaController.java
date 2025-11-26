package interfaces.controllers;

import modelo.Subtarefa;
import java.util.List;

/**
 * Interface para controller de subtarefas.
 * <p>
 * Define operações de coordenação para gestão de subtarefas,
 * seguindo o padrão GRASP Controller e o princípio DIP.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public interface ISubtarefaController {
    
    /**
     * Coordena a adição de uma subtarefa a uma tarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param titulo título da subtarefa
     * @param descricao descrição da subtarefa
     * @param percentual percentual de conclusão
     * @return true se adicionada com sucesso
     */
    boolean adicionarSubtarefa(String tituloTarefa, String titulo, String descricao, double percentual);
    
    /**
     * Coordena a edição de uma subtarefa existente.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param tituloAntigo título atual da subtarefa
     * @param novoTitulo novo título
     * @param novaDescricao nova descrição
     * @param novoPercentual novo percentual
     * @return true se editada com sucesso
     */
    boolean editarSubtarefa(String tituloTarefa, String tituloAntigo, String novoTitulo, 
                           String novaDescricao, double novoPercentual);
    
    /**
     * Coordena a remoção de uma subtarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param titulo título da subtarefa a ser removida
     * @return true se removida com sucesso
     */
    boolean removerSubtarefa(String tituloTarefa, String titulo);
    
    /**
     * Coordena a listagem de subtarefas de uma tarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @return lista de subtarefas da tarefa
     */
    List<Subtarefa> listarSubtarefas(String tituloTarefa);
}