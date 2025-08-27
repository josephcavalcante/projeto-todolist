package controle;

import modelo.Tarefa;
import modelo.Subtarefa;
import java.util.List;

// administração das subtarefas
public class SubtarefaService {
    private ManipuladorDeTarefas controlador;
    private TarefaService servicoTarefas;

    public SubtarefaService(ManipuladorDeTarefas manipulador, TarefaService tarefaService) {
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
}