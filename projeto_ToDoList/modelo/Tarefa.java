package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Entidade JPA que representa uma tarefa no sistema.
 * <p>
 * Mapeada para a tabela "tarefas" no banco de dados.
 * Possui relacionamento One-to-Many com Subtarefa.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 1.0
 */
@Entity
@Table(name = "tarefas")
public class Tarefa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;
    
    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;
    
    @Column(name = "percentual", precision = 5, scale = 2)
    private double percentual;
    
    @Column(name = "data_concretizacao")
    private LocalDate dataConcretizacao;
    
    @Column(name = "prioridade", nullable = false)
    private int prioridade;
    
    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subtarefa> subtarefas;

    /**
     * Construtor padrão para JPA.
     */
    public Tarefa() {
        this.subtarefas = new ArrayList<>();
    }
    
    /**
     * Construtor completo para criação de tarefa.
     */
    public Tarefa(String titulo, String descricao, LocalDate dataCadastro, LocalDate deadline, int prioridade) {
        this();
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
        this.deadline = deadline;
        this.prioridade = prioridade;
        this.percentual = 0.0;
    }

    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public double getPercentual() {
        if (!subtarefas.isEmpty()) {
            double soma = 0;
            for (Subtarefa s : subtarefas) {
                soma += s.getPercentual();
            }
            return soma / subtarefas.size();
        }
        return percentual;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }

    public LocalDate getDataConcretizacao() {
        return dataConcretizacao;
    }

    public void setDataConcretizacao(LocalDate dataConcretizacao) {
        this.dataConcretizacao = dataConcretizacao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public List<Subtarefa> getSubtarefas() {
        return subtarefas;
    }

    public void adicionarSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.add(subtarefa);
    }

    public void removerSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.remove(subtarefa);
    }
} 