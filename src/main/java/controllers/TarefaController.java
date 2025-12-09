package controllers;

import modelo.Tarefa;
import modelo.Usuario;
import interfaces.services.ITarefaService;
import interfaces.controllers.ITarefaController;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller simplificado que apenas delega para o Service.
 * A lógica de listagem foi removida pois deve ser gerenciada pelo Facade
 * (ToDoList)
 * que injeta o contexto do usuário.
 */
public class TarefaController implements ITarefaController {
    private ITarefaService service;

    public TarefaController(ITarefaService s) {
        this.service = s;
    }

    @Override
    public boolean adicionarTarefa(String tit, String desc, LocalDate dead, int prio, Usuario u) {
        return service.cadastrar(tit, desc, dead, prio, u);
    }

    @Override
    public boolean editarTarefa(String ant, String nov, String desc, LocalDate dead, int prio, double perc) {
        return service.editar(ant, nov, desc, dead, prio, perc);
    }

    @Override
    public boolean removerTarefa(String t) {
        return service.excluir(t);
    }

    @Override
    public Tarefa buscarTarefa(String t) {
        return service.buscarPorTitulo(t);
    }

    @Override
    public List<Tarefa> listarTodas(Usuario usuario) {
        return service.listarPorUsuario(usuario);
    }

    @Override
    public List<Tarefa> listarPorData(LocalDate data, Usuario usuario) {
        return service.listarPorDataEUsuario(data, usuario);
    }

    @Override
    public List<Tarefa> listarCriticas(Usuario usuario) {
        return service.listarCriticasPorUsuario(usuario);
    }

    @Override
    public List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy estrategia, Usuario usuario) {
        return service.listarOrdenado(estrategia, usuario);
    }
}