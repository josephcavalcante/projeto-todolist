package interfaces.strategies;

import java.util.List;
import modelo.Tarefa;

/**
 * Interface Strategy para filtragem de tarefas.
 * <p>
 * Define o contrato para algoritmos de filtragem, permitindo
 * selecionar subconjuntos de tarefas dinamicamente.
 * </p>
 */
public interface IFiltroStrategy {
    /**
     * Filtra uma lista de tarefas.
     * 
     * @param tarefas lista original
     * @return lista filtrada (subconjunto da original)
     */
    List<Tarefa> filtrar(List<Tarefa> tarefas);
}
