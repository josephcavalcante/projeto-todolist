package controllers;

import modelo.Subtarefa;
import interfaces.ISubtarefaService;
import java.util.List;

// controller especifico pra subtarefas - SRP
// so coordena operacoes de subtarefa
public class SubtarefaController {
    private ISubtarefaService subtarefaService;
    
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