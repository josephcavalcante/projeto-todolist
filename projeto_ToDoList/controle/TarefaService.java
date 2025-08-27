package controle;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

// gerenciamento das tarefas vindas da interface
public class TarefaService {
    private ManipuladorDeTarefas gerenciador;

    public TarefaService(ManipuladorDeTarefas manipulador) {
        this.gerenciador = manipulador; // armazenamento da referência
    }

    // criação de tarefa nova
    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade) {
        // validação da existência do título
        if(titulo == null || titulo.trim().equals("")) { // sem espaço no if
            return false; // título obrigatório
        }
        try {
            // instanciação da tarefa com data atual
            Tarefa novaTarefa = new Tarefa(titulo.trim(), descricao.trim(), LocalDate.now(), deadline, prioridade);
            gerenciador.adicionarTarefa(novaTarefa);
            return true; // operação bem-sucedida
        } catch (Exception ex) {
            return false; // falha na operação
        }
    }

    // edição de tarefa existente
    public boolean editar(String tituloAntigo, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade, double novoPercentual) {
        // verificação do novo título
        if (novoTitulo == null || novoTitulo.trim().length() == 0) {
            return false;
        }
        try {
            // localização da tarefa original
            Tarefa tarefaVelha = buscarPorTitulo(tituloAntigo);
            if (tarefaVelha == null) {
                return false; // tarefa inexistente
            }

            // construção da tarefa atualizada
            Tarefa tarefaEditada = new Tarefa(novoTitulo.trim(), novaDescricao.trim(), tarefaVelha.getDataCadastro(), novoDeadline, novaPrioridade);
            tarefaEditada.setPercentual(novoPercentual); // definição do percentual
            gerenciador.editarTarefa(tarefaVelha, tarefaEditada);
            return true;
        } catch (Exception erro) {
            return false; // falha na edição
        }
    }

    // remoção de tarefa
    public boolean excluir(String titulo) {
        try {
            Tarefa tarefaParaRemover = buscarPorTitulo(titulo);
            if (tarefaParaRemover != null) {
                gerenciador.removerTarefa(tarefaParaRemover);
                return true; // exclusão realizada
            }
            return false; // tarefa não localizada
        } catch (Exception ex) {
            return false; // falha na exclusão
        }
    }

    // busca de tarefa por título
    public Tarefa buscarPorTitulo(String titulo) {
        List<Tarefa> listaTarefas = gerenciador.listarTarefas();
        int contador = 0; // TODO: usar depois pra estatisticas
        // iteração sobre todas as tarefas
        for (Tarefa t : listaTarefas) {
            contador++; // incrementa contador
            if(t.getTitulo().equalsIgnoreCase(titulo)) { return t; } // inline
        }
        return null; // tarefa inexistente
    }
}