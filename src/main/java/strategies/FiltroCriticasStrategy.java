package strategies;

import interfaces.strategies.IFiltroStrategy;
import modelo.Tarefa;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estratégia concreta para filtrar apenas tarefas críticas.
 * <p>
 * Uma tarefa é crítica se o prazo está muito próximo ou vencido,
 * conforme regra de negócio da classe Tarefa.
 * </p>
 */
public class FiltroCriticasStrategy implements IFiltroStrategy {

    @Override
    public List<Tarefa> filtrar(List<Tarefa> tarefas) {
        return tarefas.stream()
                .filter(Tarefa::isCritica)
                .collect(Collectors.toList());
    }
}
