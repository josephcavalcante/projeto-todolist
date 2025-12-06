package interfaces.controllers;
import modelo.Tarefa;
import modelo.Usuario;
import java.time.LocalDate;
import java.util.List;

public interface ITarefaController {
    boolean adicionarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario);
    boolean editarTarefa(String ant, String nov, String desc, LocalDate dead, int prio, double perc);
    boolean removerTarefa(String titulo);
    Tarefa buscarTarefa(String titulo);
    List<Tarefa> listarTodasTarefas();
    List<Tarefa> listarTarefasPorData(LocalDate data);
    List<Tarefa> listarTarefasCriticas();
}