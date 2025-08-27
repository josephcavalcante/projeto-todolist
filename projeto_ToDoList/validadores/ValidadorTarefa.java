package validadores;

import interfaces.IValidadorTarefa;
import modelo.Tarefa;

/**
 * Implementação do validador de tarefas
 * Princípio SRP: Responsabilidade única de validação
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