package modelo;

import java.io.Serializable;
import jakarta.persistence.*;

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
@Entity
@Table(name = "subtarefas")
public class Subtarefa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 100)
    private String tituloSub;

    @Column(name = "descricao", length = 500)
    private String descricaoSub;

    @Column(name = "percentual", precision = 5, scale = 2)
    private double percentualConclusao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarefa_id", nullable = false)
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