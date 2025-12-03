package interfaces.strategies;

import java.util.List;
import modelo.Tarefa;

/**
 * Interface Strategy para ordenação de tarefas.
 * <p>
 * Define o contrato para algoritmos de ordenação, permitindo
 * que sejam trocados dinamicamente em tempo de execução.
 * </p>
 */
public interface IOrdenacaoStrategy {
    /**
     * Ordena uma lista de tarefas.
     * 
     * @param tarefas lista a ser ordenada
     * @return nova lista ordenada
     */
    List<Tarefa> ordenar(List<Tarefa> tarefas);
}
