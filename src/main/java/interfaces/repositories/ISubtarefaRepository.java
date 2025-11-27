package interfaces.repositories;

import modelo.Subtarefa;
import java.util.List;

public interface ISubtarefaRepository {
    void salvar(Subtarefa subtarefa);

    void remover(Subtarefa subtarefa);

    List<Subtarefa> listarPorTarefaId(Long tarefaId);

    Subtarefa buscarPorTitulo(String titulo, Long tarefaId);
}
