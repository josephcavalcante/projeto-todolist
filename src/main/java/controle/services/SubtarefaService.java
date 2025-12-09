package controle.services;

import modelo.Tarefa;
import modelo.Subtarefa;
import interfaces.services.ISubtarefaService;
import interfaces.services.ITarefaService;
import validadores.ValidadorTarefa;
import interfaces.repositories.ISubtarefaRepository;
import java.util.List;

/**
 * Service responsável pela administração de subtarefas.
 * <p>
 * Gerencia operações CRUD de subtarefas, coordenando com o TarefaService
 * para localização das tarefas pai. Aplica princípios SRP e DIP.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 1.0
 */
public class SubtarefaService implements ISubtarefaService {
    private ISubtarefaRepository repositorio;
    private ITarefaService servicoTarefas;

    public SubtarefaService(ISubtarefaRepository repositorio, ITarefaService tarefaService) {
        this.repositorio = repositorio;
        this.servicoTarefas = tarefaService;
    }

    // inclusão de subtarefa nova
    public boolean adicionar(String tituloTarefa, String tituloSub, String descricaoSub, double percentual) {
        if (tituloSub == null || tituloSub.trim().equals(""))
            return false;
        try {
            Tarefa tarefaPai = servicoTarefas.buscarPorTitulo(tituloTarefa);
            if (tarefaPai == null) {
                return false;
            }

            Subtarefa novaSub = new Subtarefa(tituloSub.trim(), descricaoSub.trim(), percentual);
            novaSub.setTarefa(tarefaPai);
            repositorio.salvar(novaSub);
            recalcularMedia(tarefaPai); // Atualiza progresso
            return true;
        } catch (Exception erro) {
            return false;
        }
    }

    // exclusão de subtarefa
    public boolean remover(String tituloTarefa, String tituloSub) {
        try {
            Tarefa tarefa = servicoTarefas.buscarPorTitulo(tituloTarefa);
            if (tarefa == null) {
                return false;
            }

            Subtarefa subtarefa = repositorio.buscarPorTitulo(tituloSub, tarefa.getId());
            if (subtarefa != null) {
                repositorio.remover(subtarefa);
                recalcularMedia(tarefa); // Atualiza progresso
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    // modificação de subtarefa (remoção + criação)
    public boolean editar(String tituloTarefa, String tituloSubAntigo, String novoTituloSub, String novaDescricaoSub,
            double novoPercentual) {
        try {
            if (remover(tituloTarefa, tituloSubAntigo)) {
                return adicionar(tituloTarefa, novoTituloSub, novaDescricaoSub, novoPercentual);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // listagem de subtarefas
    @Override
    public List<Subtarefa> listar(String tituloTarefa) {
        Tarefa tarefa = servicoTarefas.buscarPorTitulo(tituloTarefa);
        if (tarefa != null) {
            return repositorio.listarPorTarefaId(tarefa.getId());
        }
        return List.of();
    }

    /**
     * Recalcula a média de conclusão das subtarefas e atualiza a tarefa pai.
     */
    private void recalcularMedia(Tarefa tarefa) {
        List<Subtarefa> subs = repositorio.listarPorTarefaId(tarefa.getId());
        if (subs.isEmpty()) {
            servicoTarefas.atualizarPercentual(tarefa.getId(), 0.0);
            return;
        }
        double soma = 0;
        for (Subtarefa s : subs) {
            soma += s.getPercentual();
        }
        double media = soma / subs.size();
        servicoTarefas.atualizarPercentual(tarefa.getId(), media);
    }

}
