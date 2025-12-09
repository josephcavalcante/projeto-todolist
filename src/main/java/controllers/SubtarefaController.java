package controllers;

import java.util.List;

import interfaces.services.ISubtarefaService;
import interfaces.controllers.ISubtarefaController;
import modelo.Subtarefa;
import modelo.Usuario;

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
public class SubtarefaController implements ISubtarefaController {
    private ISubtarefaService subtarefaService;

    /**
     * Construtor que injeta a dependência do service.
     * 
     * @param subtarefaService service de subtarefas
     */
    public SubtarefaController(ISubtarefaService subtarefaService) {
        this.subtarefaService = subtarefaService;
    }

    /**
     * Coordena a adição de uma subtarefa a uma tarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param titulo       título da subtarefa
     * @param descricao    descrição da subtarefa
     * @param percentual   percentual de conclusão
     * @param usuario      usuário dono da tarefa
     * @return true se adicionada com sucesso
     */
    @Override
    public boolean adicionarSubtarefa(String tituloTarefa, String titulo, String descricao, double percentual,
            Usuario usuario) {
        return subtarefaService.adicionar(tituloTarefa, titulo, descricao, percentual, usuario);
    }

    /**
     * Coordena a edição de uma subtarefa existente.
     * 
     * @param tituloTarefa   título da tarefa pai
     * @param tituloAntigo   título atual da subtarefa
     * @param novoTitulo     novo título
     * @param novaDescricao  nova descrição
     * @param novoPercentual novo percentual
     * @param usuario        usuário dono da tarefa
     * @return true se editada com sucesso
     */
    @Override
    public boolean editarSubtarefa(String tituloTarefa, String tituloAntigo, String novoTitulo,
            String novaDescricao, double novoPercentual, Usuario usuario) {
        return subtarefaService.editar(tituloTarefa, tituloAntigo, novoTitulo, novaDescricao, novoPercentual, usuario);
    }

    /**
     * Coordena a remoção de uma subtarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param titulo       título da subtarefa a ser removida
     * @param usuario      usuário dono da tarefa
     * @return true se removida com sucesso
     */
    @Override
    public boolean removerSubtarefa(String tituloTarefa, String titulo, Usuario usuario) {
        return subtarefaService.remover(tituloTarefa, titulo, usuario);
    }

    /**
     * Coordena a listagem de subtarefas de uma tarefa.
     * 
     * @param tituloTarefa título da tarefa pai
     * @param usuario      usuário dono da tarefa
     * @return lista de subtarefas da tarefa
     */
    @Override
    public List<Subtarefa> listarSubtarefas(String tituloTarefa, Usuario usuario) {
        return subtarefaService.listar(tituloTarefa, usuario);
    }
}