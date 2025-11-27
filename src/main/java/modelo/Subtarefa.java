package modelo;

import java.io.Serializable;

/**
 * Entidade JPA que representa uma subtarefa no sistema.
 * <p>
 * Mapeada para a tabela "subtarefas" no banco de dados.
 * Possui relacionamento Many-to-One com Tarefa.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 1.0
 */
public class Subtarefa implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String tituloSub;

    private String descricaoSub;

    private double percentualConclusao;

    private Tarefa tarefa;

    /**
     * Construtor padrão para JPA.
     */
    public Subtarefa() {
    }

    /**
     * Construtor completo para criação de subtarefa.
     */
    public Subtarefa(String titulo, String descricao, double percentual) {
        this.tituloSub = titulo;
        this.descricaoSub = descricao;
        this.percentualConclusao = percentual;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return tituloSub;
    }

    public void setTitulo(String titulo) {
        this.tituloSub = titulo;
    }

    public String getDescricao() {
        return descricaoSub;
    }

    public void setDescricao(String descricao) {
        this.descricaoSub = descricao;
    }

    public double getPercentual() {
        return percentualConclusao;
    }

    public void setPercentual(double percentual) {
        this.percentualConclusao = percentual;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }
}