package controle;

import modelo.Tarefa;
import modelo.Subtarefa;
import modelo.Usuario;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// controle principal das tarefas do sistema
public class ManipuladorDeTarefas implements Serializable {
    private List<Tarefa> listaTarefas;
    private Usuario usuarioSistema;

    public ManipuladorDeTarefas() {
        this.listaTarefas = new ArrayList<>(); // inicialização da lista vazia
    }

    // inclusão de tarefa na lista
    public void adicionarTarefa(Tarefa tarefa) {
        listaTarefas.add(tarefa);
    }

    // exclusão de tarefa da lista
    public void removerTarefa(Tarefa tarefa) {
        listaTarefas.remove(tarefa);
    }

    // substituição de tarefa existente
    public void editarTarefa(Tarefa antiga, Tarefa nova) {
        int indice = listaTarefas.indexOf(antiga);
        if (indice != -1) {
            listaTarefas.set(indice, nova); // substituição no índice encontrado
        }
    }

    // obtenção de todas as tarefas cadastradas
    public List<Tarefa> listarTarefas() {
        return new ArrayList<>(listaTarefas); // cópia da lista original
    }

    // filtragem de tarefas por data específica
    public List<Tarefa> listarTarefasPorData(LocalDate data) {
        return listaTarefas.stream()
                .filter(tarefa -> tarefa.getDeadline().equals(data))
                .collect(Collectors.toList());
    }

    // identificação de tarefas críticas (prazo vencendo)
    public List<Tarefa> listarTarefasCriticas() {
        LocalDate dataAtual = LocalDate.now();
        return listaTarefas.stream()
                .filter(tarefa -> tarefa.getDeadline().minusDays(tarefa.getPrioridade()).isBefore(dataAtual))
                .collect(Collectors.toList());
    }

    // adição de subtarefa numa tarefa
    public void adicionarSubtarefa(Tarefa tarefa, Subtarefa subtarefa) {
        tarefa.adicionarSubtarefa(subtarefa);
    }

    // remoção de subtarefa de uma tarefa
    public void removerSubtarefa(Tarefa tarefa, Subtarefa subtarefa) {
        tarefa.removerSubtarefa(subtarefa);
    }

    // listagem das subtarefas de uma tarefa
    public List<Subtarefa> listarSubtarefas(Tarefa tarefa) {
        return tarefa.getSubtarefas();
    }

    // acesso ao usuário do sistema
    public Usuario getUsuario() {
        return usuarioSistema;
    }

    // definição do usuário do sistema
    public void setUsuario(Usuario usuario) {
        this.usuarioSistema = usuario;
    }

    // cadastramento de tarefa com conversão de data
    public void cadastrarTarefa(String titulo, String descricao, String deadlineStr, int prioridade) throws Exception {
        try {
            // conversão DD/MM/AAAA para LocalDate
            String[] partesData = deadlineStr.split("/");
            int dia = Integer.parseInt(partesData[0]);
            int mes = Integer.parseInt(partesData[1]);
            int ano = Integer.parseInt(partesData[2]);
            
            LocalDate prazoFinal = LocalDate.of(ano, mes, dia);
            LocalDate dataAtual = LocalDate.now();
            
            Tarefa novaTarefa = new Tarefa(titulo, descricao, dataAtual, prazoFinal, prioridade);
            adicionarTarefa(novaTarefa);
        } catch (Exception erro) {
            throw new Exception("Data inválida. Use o formato DD/MM/AAAA");
        }
    }
} 