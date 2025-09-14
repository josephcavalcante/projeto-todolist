package controllers;

import modelo.Subtarefa;
import interfaces.ISubtarefaService;
import java.util.List;

/**
 * Controller responsável por coordenar operações de subtarefas.
 * <p>
 * Atua como camada de coordenação entre a interface e o SubtarefaService,
 * seguindo o padrão GRASP Controller e mantendo baixo acoplamento.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public class SubtarefaController {
    private ISubtarefaService subtarefaService;
    
    /**
     * Construtor que injeta a dependência do service.
     * 
     * @param subtarefaService service de subtarefas
     */
    public SubtarefaController(ISubtarefaService subtarefaService) {
        this.subtarefaService = subtarefaService;
    }
    
    public boolean adicionarSubtarefa(String tituloTarefa, String titulo, String descricao, double percentual) {
        return subtarefaService.adicionar(tituloTarefa, titulo, descricao, percentual);
    }
    
    public boolean editarSubtarefa(String tituloTarefa, String tituloAntigo, String novoTitulo, 
                                  String novaDescricao, double novoPercentual) {
        return subtarefaService.editar(tituloTarefa, tituloAntigo, novoTitulo, novaDescricao, novoPercentual);
    }
    
    public boolean removerSubtarefa(String tituloTarefa, String titulo) {
        return subtarefaService.remover(tituloTarefa, titulo);
    }
    
    public List<Subtarefa> listarSubtarefas(String tituloTarefa) {
        return subtarefaService.listar(tituloTarefa);
    }
}