package controllers;

import modelo.Tarefa;
import interfaces.ITarefaService;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsável por coordenar operações relacionadas a tarefas.
 * <p>
 * Atua como uma camada de coordenação entre a interface do usuário e os services,
 * seguindo o padrão GRASP Controller. Mantém baixo acoplamento delegando
 * a lógica de negócio para o TarefaService.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public class TarefaController {
    private ITarefaService tarefaService;
    
    public TarefaController(ITarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }
    
    public boolean adicionarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        return tarefaService.cadastrar(titulo, descricao, deadline, prioridade);
    }
    
    public boolean editarTarefa(String tituloAntigo, String novoTitulo, String novaDescricao, 
                               LocalDate novoDeadline, int novaPrioridade, double novoPercentual) {
        return tarefaService.editar(tituloAntigo, novoTitulo, novaDescricao, novoDeadline, novaPrioridade, novoPercentual);
    }
    
    public boolean removerTarefa(String titulo) {
        return tarefaService.excluir(titulo);
    }
    
    public Tarefa buscarTarefa(String titulo) {
        return tarefaService.buscarPorTitulo(titulo);
    }
    
    // metodos de listagem - delegacao pro service
    public List<Tarefa> listarTodasTarefas() {
        return tarefaService.listarTodas();
    }
    
    public List<Tarefa> listarTarefasPorData(LocalDate data) {
        return tarefaService.listarPorData(data);
    }
    
    public List<Tarefa> listarTarefasCriticas() {
        return tarefaService.listarCriticas();
    }
}