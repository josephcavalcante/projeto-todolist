package strategies;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import interfaces.strategies.IOrdenacaoStrategy;
import modelo.Tarefa;

/**
 * Estratégia de ordenação por data (Deadline).
 */
public class OrdenacaoPorDataStrategy implements IOrdenacaoStrategy {

    @Override
    public List<Tarefa> ordenar(List<Tarefa> tarefas) {
        return tarefas.stream()
                .sorted(Comparator.comparing(Tarefa::getDeadline))
                .collect(Collectors.toList());
    }
}
