package strategies;

import interfaces.strategies.IFiltroStrategy;
import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estratégia para filtrar tarefas por uma data específica (Deadline).
 */
public class FiltroPorDataStrategy implements IFiltroStrategy {
    private final LocalDate dataAlvo;

    public FiltroPorDataStrategy(LocalDate dataAlvo) {
        this.dataAlvo = dataAlvo;
    }

    @Override
    public List<Tarefa> filtrar(List<Tarefa> tarefas) {
        if (dataAlvo == null) {
            return tarefas; // Se data é null, retorna tudo (ou podia retornar vazio)
        }
        return tarefas.stream()
                .filter(t -> t.getDeadline().equals(dataAlvo))
                .collect(Collectors.toList());
    }
}
