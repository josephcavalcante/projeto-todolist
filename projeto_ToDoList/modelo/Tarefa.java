package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tarefa implements Serializable {
    private String titulo;
    private String descricao;
    private LocalDate dataCadastro;
    private LocalDate deadline;
    private double percentual;
    private LocalDate dataConcretizacao;
    private int prioridade; 
    private List<Subtarefa> subtarefas;

    public Tarefa(String titulo, String descricao, LocalDate dataCadastro, LocalDate deadline, int prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
        this.deadline = deadline;
        this.prioridade = prioridade;
        this.percentual = 0.0;
        this.subtarefas = new ArrayList<>();
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