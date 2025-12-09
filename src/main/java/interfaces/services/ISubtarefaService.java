package interfaces.services;

import java.util.List;

import modelo.Subtarefa;
import modelo.Usuario;

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
     * @param titulo       título da subtarefa
     * @param descricao    descrição da subtarefa
     * @param percentual   percentual de conclusão inicial
     * @param usuario      usuário dono da tarefa
     * @return true se a subtarefa foi adicionada com sucesso, false caso contrário
     */
    boolean adicionar(String tituloTarefa, String titulo, String descricao, double percentual, Usuario usuario);

    /**
     * Edita uma subtarefa existente.
     * 
     * @param tituloTarefa   título da tarefa pai
     * @param tituloAntigo   título atual da subtarefa
     * @param novoTitulo     novo título da subtarefa
     * @param novaDescricao  nova descrição da subtarefa
     * @param novoPercentual novo percentual de conclusão
     * @param usuario        usuário dono da tarefa
     * @return true se a edição foi bem-sucedida, false caso contrário
     */
    boolean editar(String tituloTarefa, String tituloAntigo, String novoTitulo, String novaDescricao,
            double novoPercentual, Usuario usuario);

    /**
     * Remove uma subtarefa de uma tarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param titulo       título da subtarefa a ser removida
     * @param usuario      usuário dono da tarefa
     * @return true se a remoção foi bem-sucedida, false caso contrário
     */
    boolean remover(String tituloTarefa, String titulo, Usuario usuario);

    /**
     * Lista todas as subtarefas de uma tarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param usuario      usuário dono da tarefa
     * @return lista de subtarefas ou lista vazia se não houver subtarefas
     */
    List<Subtarefa> listar(String tituloTarefa, Usuario usuario);
}