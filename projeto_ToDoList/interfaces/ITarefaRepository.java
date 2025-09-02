package interfaces;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

// interface pro repositorio de tarefas
// separei as operacoes de dados aqui pra ficar mais organizado
public interface ITarefaRepository {
    void salvar(Tarefa tarefa);
    void remover(Tarefa tarefa);
    void atualizar(Tarefa antiga, Tarefa nova);
    List<Tarefa> listarTodas();
    List<Tarefa> listarPorData(LocalDate data);
    Tarefa buscarPorTitulo(String titulo);
}