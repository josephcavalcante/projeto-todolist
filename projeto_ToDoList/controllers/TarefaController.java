package controllers;

import modelo.Tarefa;
import interfaces.services.ITarefaService;

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
    
    /**
     * Construtor que injeta o service de tarefas.
     * 
     * @param tarefaService service responsável pela lógica de negócio
     */
    public TarefaController(ITarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }
    
    /**
     * Coordena a adição de uma nova tarefa.
     * 
     * @param titulo título da tarefa
     * @param descricao descrição detalhada
     * @param deadline prazo limite
     * @param prioridade nível de prioridade (1-5)
     * @return true se adicionada com sucesso
     */
    public boolean adicionarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        return tarefaService.cadastrar(titulo, descricao, deadline, prioridade);
    }
    
    /**
     * Coordena a edição de uma tarefa existente.
     * 
     * @param tituloAntigo título atual da tarefa
     * @param novoTitulo novo título
     * @param novaDescricao nova descrição
     * @param novoDeadline novo prazo
     * @param novaPrioridade nova prioridade
     * @param novoPercentual novo percentual de conclusão
     * @return true se editada com sucesso
     */
    public boolean editarTarefa(String tituloAntigo, String novoTitulo, String novaDescricao, 
                               LocalDate novoDeadline, int novaPrioridade, double novoPercentual) {
        return tarefaService.editar(tituloAntigo, novoTitulo, novaDescricao, novoDeadline, novaPrioridade, novoPercentual);
    }
    
    /**
     * Coordena a remoção de uma tarefa.
     * 
     * @param titulo título da tarefa a ser removida
     * @return true se removida com sucesso
     */
    public boolean removerTarefa(String titulo) {
        return tarefaService.excluir(titulo);
    }
    
    /**
     * Coordena a busca de uma tarefa por título.
     * 
     * @param titulo título da tarefa procurada
     * @return tarefa encontrada ou null
     */
    public Tarefa buscarTarefa(String titulo) {
        return tarefaService.buscarPorTitulo(titulo);
    }
    
    /**
     * Coordena a listagem de todas as tarefas.
     * 
     * @return lista com todas as tarefas
     */
    public List<Tarefa> listarTodasTarefas() {
        return tarefaService.listarTodas();
    }
    
    /**
     * Coordena a listagem de tarefas por data específica.
     * 
     * @param data data para filtrar as tarefas
     * @return lista de tarefas da data especificada
     */
    public List<Tarefa> listarTarefasPorData(LocalDate data) {
        return tarefaService.listarPorData(data);
    }
    
    /**
     * Coordena a listagem de tarefas críticas (prazo vencendo).
     * 
     * @return lista de tarefas com prazo crítico
     */
    public List<Tarefa> listarTarefasCriticas() {
        return tarefaService.listarCriticas();
    }
}