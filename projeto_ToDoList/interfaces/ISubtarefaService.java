package interfaces;

import modelo.Tarefa;
import modelo.Subtarefa;
import java.util.List;

/**
 * Interface para serviços de gestão de subtarefas.
 * <p>
 * Define operações CRUD para subtarefas, mantendo baixo acoplamento
 * e seguindo o princípio ISP (Interface Segregation Principle).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public interface ISubtarefaService {
    /**
     * Adiciona uma nova subtarefa a uma tarefa existente.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param titulo título da subtarefa
     * @param descricao descrição da subtarefa
     * @param percentual percentual de conclusão inicial
     * @return true se a subtarefa foi adicionada com sucesso, false caso contrário
     */
    boolean adicionar(String tituloTarefa, String titulo, String descricao, double percentual);
    
    /**
     * Edita uma subtarefa existente.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param tituloAntigo título atual da subtarefa
     * @param novoTitulo novo título da subtarefa
     * @param novaDescricao nova descrição da subtarefa
     * @param novoPercentual novo percentual de conclusão
     * @return true se a edição foi bem-sucedida, false caso contrário
     */
    boolean editar(String tituloTarefa, String tituloAntigo, String novoTitulo, String novaDescricao, double novoPercentual);
    
    /**
     * Remove uma subtarefa de uma tarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param titulo título da subtarefa a ser removida
     * @return true se a remoção foi bem-sucedida, false caso contrário
     */
    boolean remover(String tituloTarefa, String titulo);
    
    /**
     * Lista todas as subtarefas de uma tarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @return lista de subtarefas ou lista vazia se não houver subtarefas
     */
    List<Subtarefa> listar(String tituloTarefa);
}