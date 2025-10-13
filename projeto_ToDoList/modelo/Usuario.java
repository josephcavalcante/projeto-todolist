package modelo;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entidade JPA que representa um usuário no sistema.
 * <p>
 * Mapeada para a tabela "usuarios" no banco de dados.
 * Email é único e imutável após criação.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 1.0
 */
@Entity
@Table(name = "usuarios", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nome_usuario", nullable = false, length = 100)
    private String nomeUsuario;
    
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String emailFixo; // não pode ser alterado

    /**
     * Construtor padrão para JPA.
     */
    public Usuario() {
    }

    /**
     * Construtor completo para criação de usuário.
     */
    public Usuario(String nome, String email) {
        this.nomeUsuario = nome;
        this.emailFixo = email;
    }

    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nomeUsuario;
    }

    public void setNome(String nome) {
        this.nomeUsuario = nome;
    }

    public String getEmail() {
        return emailFixo;
    }
    
    /**
     * Método interno para JPA definir email.
     * Não deve ser usado diretamente no código.
     */
    protected void setEmail(String email) {
        this.emailFixo = email;
    }
    
    @Override
    public String toString() {
        return String.format("Usuario{id=%d, nome='%s', email='%s'}", id, nomeUsuario, emailFixo);
    }
}