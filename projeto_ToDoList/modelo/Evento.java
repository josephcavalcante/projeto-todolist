package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Entidade que representa um evento no sistema.
 * <p>
 * Um evento possui título, descrição, data e local. Aplica o princípio
 * Information Expert calculando os dias restantes para o evento.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String titulo;
    private String descricao;
    private LocalDate dataEvento;
    private String local;
    private LocalDate dataCadastro;

    /**
     * Construtor completo do evento.
     * 
     * @param titulo título do evento (obrigatório)
     * @param descricao descrição detalhada do evento
     * @param dataEvento data em que o evento ocorrerá
     * @param local local onde o evento acontecerá
     */
    public Evento(String titulo, String descricao, LocalDate dataEvento, String local) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataEvento = dataEvento;
        this.local = local;
        this.dataCadastro = LocalDate.now();
    }

    /**
     * Calcula quantos dias faltam para o evento.
     * <p>
     * Aplica Information Expert - o evento sabe calcular seus próprios dias restantes.
     * </p>
     * 
     * @return número de dias até o evento (negativo se já passou)
     */
    public long diasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), this.dataEvento);
    }

    /**
     * Verifica se o evento já passou.
     * 
     * @return true se o evento já ocorreu, false caso contrário
     */
    public boolean jaPassou() {
        return this.dataEvento.isBefore(LocalDate.now());
    }

    /**
     * Verifica se o evento é hoje.
     * 
     * @return true se o evento é hoje, false caso contrário
     */
    public boolean ehHoje() {
        return this.dataEvento.equals(LocalDate.now());
    }

    // Getters e Setters
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

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return String.format("Evento{titulo='%s', data=%s, local='%s', diasRestantes=%d}", 
                           titulo, dataEvento, local, diasRestantes());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Evento evento = (Evento) obj;
        return titulo.equals(evento.titulo) && dataEvento.equals(evento.dataEvento);
    }

    @Override
    public int hashCode() {
        return titulo.hashCode() + dataEvento.hashCode();
    }
}