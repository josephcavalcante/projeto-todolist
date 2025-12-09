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
    public boolean editarTarefa(String ant, String nov, String desc, LocalDate dead, int prio, double perc,
            Usuario usuario) {
        return service.editar(ant, nov, desc, dead, prio, perc, usuario);
    }

    @Override
    public boolean removerTarefa(String t, Usuario usuario) {
        return service.excluir(t, usuario);
    }

    @Override
    public Tarefa buscarTarefa(String t, Usuario usuario) {
        return service.buscarPorTitulo(t, usuario);
    }

    @Override
    public List<Tarefa> listarTodas(Usuario usuario) {
        return service.listarPorUsuario(usuario);
    }

    @Override
    public List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy estrategia, Usuario usuario) {
        return service.listarOrdenado(estrategia, usuario);
    }

    @Override
    public List<Tarefa> listar(interfaces.strategies.IFiltroStrategy estrategia, Usuario usuario) {
        return service.listar(estrategia, usuario);
    }
}