package controllers;

import modelo.Tarefa;
import controle.TarefaService;
import java.time.LocalDate;
import java.util.List;

// controller especifico pra tarefas - SRP aplicado
// so coordena operacoes de tarefa, nada mais
public class TarefaController {
    private TarefaService tarefaService;
    
    public TarefaController(TarefaService tarefaService) {
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
}