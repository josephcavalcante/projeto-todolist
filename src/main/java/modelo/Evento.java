package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.persistence.*;

/**
 * Entidade JPA que representa um evento no sistema.
 * <p>
 * Mapeada para a tabela "eventos" no banco de dados.
 * Um evento possui título, descrição, data e local. Aplica o princípio
 * Information Expert calculando os dias restantes para o evento.
 * Constraint de unicidade na data impede conflito de eventos.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
@Entity
@Table(name = "eventos")
public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @Column(name = "data_evento", nullable = false, unique = true)
    private LocalDate dataEvento;
    
    @Column(name = "local", length = 200)
    private String local;
    
    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    /**
     * Construtor padrão para JPA.
     */
    public Evento() {
    }
    
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