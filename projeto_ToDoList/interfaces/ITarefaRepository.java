package interfaces;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para repositório de tarefas
 * Princípio ISP: Interface específica para operações de tarefa
 * Princípio DIP: Abstração para inversão de dependência
 */
public interface ITarefaRepository {
    void salvar(Tarefa tarefa);
    void remover(Tarefa tarefa);
    void atualizar(Tarefa antiga, Tarefa nova);
    List<Tarefa> listarTodas();
    List<Tarefa> listarPorData(LocalDate data);
    Tarefa buscarPorTitulo(String titulo);
}