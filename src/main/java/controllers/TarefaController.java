package controllers;
import modelo.Tarefa;
import modelo.Usuario;
import interfaces.services.ITarefaService;
import interfaces.controllers.ITarefaController;
import java.time.LocalDate;
import java.util.List;

public class TarefaController implements ITarefaController {
    private ITarefaService service;
    public TarefaController(ITarefaService s) { this.service = s; }
    
    public boolean adicionarTarefa(String tit, String desc, LocalDate dead, int prio, Usuario u) {
        return service.cadastrar(tit, desc, dead, prio, u);
    }
    public boolean editarTarefa(String ant, String nov, String desc, LocalDate dead, int prio, double perc) {
        return service.editar(ant, nov, desc, dead, prio, perc);
    }
    public boolean removerTarefa(String t) { return service.excluir(t); }
    public Tarefa buscarTarefa(String t) { return service.buscarPorTitulo(t); }
    public List<Tarefa> listarTodasTarefas() { return service.listarTodas(); }
    public List<Tarefa> listarTarefasPorData(LocalDate d) { return service.listarPorData(d); }
    public List<Tarefa> listarTarefasCriticas() { return service.listarCriticas(); }
}