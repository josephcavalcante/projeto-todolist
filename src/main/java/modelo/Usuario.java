package modelo;

import java.io.Serializable;
import jakarta.persistence.*;

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

    @Column(name = "senha", nullable = true, length = 255)
    private String senha; // para autenticação do usuário

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

    /**
     * Construtor completo com senha para criação de usuário.
     */
    public Usuario(String nome, String email, String senha) {
        this.nomeUsuario = nome;
        this.emailFixo = email;
        this.senha = senha;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Verifica se o usuário tem senha definida.
     * 
     * @return true se tem senha, false caso contrário
     */
    public boolean temSenha() {
        return senha != null && !senha.trim().isEmpty();
    }

    /**
     * Verifica se a senha fornecida está correta.
     * 
     * @param senhaFornecida senha a ser verificada
     * @return true se a senha está correta, false caso contrário
     */
    public boolean verificarSenha(String senhaFornecida) {
        return senha != null && senha.equals(senhaFornecida);
    }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, nome='%s', email='%s', temSenha=%s}",
                id, nomeUsuario, emailFixo, temSenha());
    }
}