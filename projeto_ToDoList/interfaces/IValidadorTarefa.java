package interfaces;

import modelo.Tarefa;

/**
 * Interface para validação de tarefas
 * Princípio SRP: Responsabilidade única de validação
 */
public interface IValidadorTarefa {
    boolean validarTitulo(String titulo);
    boolean validarDescricao(String descricao);
    boolean validarTarefa(Tarefa tarefa);
}