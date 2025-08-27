package modelo;

import java.io.Serializable;

// modelo de subtarefa do sistema
public class Subtarefa implements Serializable {
    private String tituloSub;
    private String descricaoSub;
    private double percentualConclusao;

    // construção da subtarefa
    public Subtarefa(String titulo, String descricao, double percentual) {
        this.tituloSub = titulo;
        this.descricaoSub = descricao;
        this.percentualConclusao = percentual;
    }

    // acesso ao título
    public String getTitulo() {
        return tituloSub;
    }

    // modificação do título
    public void setTitulo(String titulo) {
        this.tituloSub = titulo;
    }

    // obtenção da descrição
    public String getDescricao() {
        return descricaoSub;
    }

    // alteração da descrição
    public void setDescricao(String descricao) {
        this.descricaoSub = descricao;
    }

    // consulta do percentual de conclusão
    public double getPercentual() {
        return percentualConclusao;
    }

    // atualização do percentual
    public void setPercentual(double percentual) {
        this.percentualConclusao = percentual;
    }
} 