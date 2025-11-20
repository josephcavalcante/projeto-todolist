package validadores;

import interfaces.validators.IValidadorTarefa;
import modelo.Tarefa;

/**
 * Implementação simples do validador de tarefas.
 * <p>
 * Fornece validações básicas para tarefas, seguindo o princípio SRP.
 * Pode ser substituída por implementações mais rigorosas via padrão Strategy.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 1.1
 */
public class ValidadorTarefa implements IValidadorTarefa {
    
    @Override
    public boolean validarTitulo(String titulo) {
        return titulo != null && !titulo.trim().isEmpty();
    }
    
    @Override
    public boolean validarDescricao(String descricao) {
        return descricao != null;
    }
    
    @Override
    public boolean validarTarefa(Tarefa tarefa) {
        return tarefa != null && 
               validarTitulo(tarefa.getTitulo()) && 
               validarDescricao(tarefa.getDescricao()) &&
               tarefa.getDeadline() != null;
    }
}