package builders;

import java.time.LocalDate;
import modelo.Tarefa;

/**
 * Builder para a classe Tarefa.
 * <p>
 * Facilita a criação de objetos Tarefa, permitindo uma construção fluente
 * e lidando com valores padrão (data de cadastro = hoje, percentual = 0).
 * </p>
 */
public class TarefaBuilder {
    private String titulo;
    private String descricao;
    private LocalDate dataCadastro;
    private LocalDate deadline;
    private int prioridade;
    private double percentual;

    public TarefaBuilder() {
        // Valores padrão
        this.dataCadastro = LocalDate.now();
        this.percentual = 0.0;
        this.prioridade = 1; // Prioridade mínima padrão
        this.descricao = "";
    }

    public TarefaBuilder comTitulo(String titulo) {
        this.titulo = titulo != null ? titulo.trim() : null;
        return this;
    }

    public TarefaBuilder comDescricao(String descricao) {
        this.descricao = descricao != null ? descricao.trim() : "";
        return this;
    }

    public TarefaBuilder comDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public TarefaBuilder comPrazo(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    public TarefaBuilder comPrioridade(int prioridade) {
        this.prioridade = prioridade;
        return this;
    }

    public TarefaBuilder comPercentual(double percentual) {
        this.percentual = percentual;
        return this;
    }

    /**
     * Constrói a instância de Tarefa.
     * 
     * @return nova instância de Tarefa
     * @throws IllegalStateException se dados obrigatórios estiverem faltando
     */
    public Tarefa construir() {
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalStateException("Título da tarefa é obrigatório.");
        }
        if (deadline == null) {
            throw new IllegalStateException("Data limite (Deadline) é obrigatória.");
        }

        Tarefa tarefa = new Tarefa(titulo, descricao, dataCadastro, deadline, prioridade);
        tarefa.setPercentual(percentual);
        return tarefa;
    }
}
