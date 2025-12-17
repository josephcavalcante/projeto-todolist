package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_usuario", nullable = false, length = 100)
    private String nomeUsuario;
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String emailFixo;
    @Column(length = 255)
    private String senha;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.nomeUsuario = nome;
        this.emailFixo = email;
        this.senha = senha;
    }

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean temSenha() {
        return senha != null && !senha.trim().isEmpty();
    }

    public boolean verificarSenha(String s) {
        return senha != null && senha.equals(s);
    }
}