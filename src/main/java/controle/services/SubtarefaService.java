package controle.services;

import modelo.Tarefa;
import modelo.Subtarefa;
import interfaces.services.ISubtarefaService;
import interfaces.services.ITarefaService;
import validadores.ValidadorTarefa;
import controle.ManipuladorDeTarefas;
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
    private ManipuladorDeTarefas controlador;
    private ITarefaService servicoTarefas;

    public SubtarefaService(ManipuladorDeTarefas manipulador, ITarefaService tarefaService) {
        this.controlador = manipulador;
        this.servicoTarefas = tarefaService; // dependência para localização de tarefas
    }

    // inclusão de subtarefa nova
    public boolean adicionar(String tituloTarefa, String tituloSub, String descricaoSub, double percentual) {
        // validação da obrigatoriedade do título
        if(tituloSub == null || tituloSub.trim().equals("")) return false; // inline
        try {
            // localização da tarefa principal
            Tarefa tarefaPai = servicoTarefas.buscarPorTitulo(tituloTarefa);
            if(tarefaPai == null) { // sem espaço
                return false; // tarefa principal inexistente
            }

            // instanciação da subtarefa
            Subtarefa novaSub = new Subtarefa(tituloSub.trim(), descricaoSub.trim(), percentual);
            controlador.adicionarSubtarefa(tarefaPai, novaSub);
            return true; // operação concluída
        } catch (Exception erro) {
            return false; // falha na operação
        }
    }

    // exclusão de subtarefa
    public boolean remover(String tituloTarefa, String tituloSub) {
        try {
            // localização da tarefa principal
            Tarefa tarefa = servicoTarefas.buscarPorTitulo(tituloTarefa);
            if (tarefa == null) {
                return false; // tarefa principal inexistente
            }

            // busca da subtarefa para remoção
            List<Subtarefa> listaSubs = controlador.listarSubtarefas(tarefa);
            for (Subtarefa subtarefa : listaSubs) {
                if (subtarefa.getTitulo().equals(tituloSub)) {
                    controlador.removerSubtarefa(tarefa, subtarefa);
                    return true; // remoção efetuada
                }
            }
            return false; // subtarefa não encontrada
        } catch (Exception ex) {
            return false; // falha na remoção
        }
    }

    // modificação de subtarefa (remoção + criação)
    public boolean editar(String tituloTarefa, String tituloSubAntigo, String novoTituloSub, String novaDescricaoSub, double novoPercentual) {
        try {
            // estratégia: exclusão da antiga + inclusão da nova
            if (remover(tituloTarefa, tituloSubAntigo)) {
                return adicionar(tituloTarefa, novoTituloSub, novaDescricaoSub, novoPercentual);
            }
            return false; // falha na remoção inicial
        } catch (Exception e) {
            return false; // falha na edição
        }
    }
    
    // listagem de subtarefas - metodo da interface
    @Override
    public List<Subtarefa> listar(String tituloTarefa) {
        Tarefa tarefa = servicoTarefas.buscarPorTitulo(tituloTarefa);
        if (tarefa != null) {
            return controlador.listarSubtarefas(tarefa);
        }
        return List.of(); // lista vazia se tarefa nao existe
    }
}