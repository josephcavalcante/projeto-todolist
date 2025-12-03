package strategies;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import interfaces.strategies.IOrdenacaoStrategy;
import modelo.Tarefa;

/**
 * Estratégia de ordenação por prioridade (Decrescente).
 * Prioridade 5 vem antes de Prioridade 1.
 */
public class OrdenacaoPorPrioridadeStrategy implements IOrdenacaoStrategy {

    @Override
    public List<Tarefa> ordenar(List<Tarefa> tarefas) {
        return tarefas.stream()
                .sorted(Comparator.comparingInt(Tarefa::getPrioridade).reversed())
                .collect(Collectors.toList());
    }
}
