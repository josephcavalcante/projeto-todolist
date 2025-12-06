package modelo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity @Table(name = "tarefas")
public class Tarefa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String titulo;
    @Column(length = 500) private String descricao;
    @Column(nullable = false) private LocalDate dataCadastro;
    @Column(nullable = false) private LocalDate deadline;
    @Column(name = "percentual") private double percentual; // Sem scale/precision
    private LocalDate dataConcretizacao;
    @Column(nullable = false) private int prioridade;

    @Transient private List<Subtarefa> subtarefas;

    @ManyToOne @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Tarefa() { this.subtarefas = new ArrayList<>(); }
    
    public Tarefa(String titulo, String descricao, LocalDate dataCadastro, LocalDate deadline, int prioridade) {
        this();
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
        this.deadline = deadline;
        this.prioridade = prioridade;
    }

    // Getters e Setters básicos omitidos para brevidade, gere-os na IDE se precisar ou copie do original
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String t) { this.titulo = t; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String d) { this.descricao = d; }
    public LocalDate getDataCadastro() { return dataCadastro; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate d) { this.deadline = d; }
    public int getPrioridade() { return prioridade; }
    public double getPercentual() { return percentual; }
    public void setPercentual(double p) { this.percentual = p; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public boolean isCritica() {
        LocalDate critico = deadline.minusDays(prioridade);
        return LocalDate.now().isAfter(critico) || LocalDate.now().equals(critico);
    }
}