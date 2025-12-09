package interfaces.controllers;

import modelo.Tarefa;
import modelo.Usuario;
import java.time.LocalDate;
import java.util.List;

public interface ITarefaController {
    boolean adicionarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario);

    boolean editarTarefa(String ant, String nov, String desc, LocalDate dead, int prio, double perc, Usuario usuario);

    boolean removerTarefa(String titulo, Usuario usuario);

    Tarefa buscarTarefa(String titulo, Usuario usuario);

    List<Tarefa> listarTodas(Usuario usuario);

    List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy estrategia, Usuario usuario);

    /**
     * Lista tarefas usando uma estratégia de filtragem.
     * 
     * @param estrategia estratégia de filtro a ser usada
     * @param usuario    usuário dono das tarefas
     * @return lista de tarefas filtradas
     */
    List<Tarefa> listar(interfaces.strategies.IFiltroStrategy estrategia, Usuario usuario);
}