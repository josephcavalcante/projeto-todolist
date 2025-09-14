package interfaces;

import modelo.Tarefa;

/**
 * Interface para validadores de tarefa.
 * <p>
 * Define operações de validação de tarefas, permitindo diferentes
 * implementações (simples, rigorosa, etc.) seguindo o padrão Strategy.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 1.1
 */
public interface IValidadorTarefa {
    /**
     * Valida o título de uma tarefa.
     * 
     * @param titulo título a ser validado
     * @return true se o título é válido, false caso contrário
     */
    boolean validarTitulo(String titulo);
    
    /**
     * Valida a descrição de uma tarefa.
     * 
     * @param descricao descrição a ser validada
     * @return true se a descrição é válida, false caso contrário
     */
    boolean validarDescricao(String descricao);
    
    /**
     * Valida uma tarefa completa.
     * 
     * @param tarefa tarefa a ser validada
     * @return true se a tarefa é válida, false caso contrário
     */
    boolean validarTarefa(Tarefa tarefa);
}