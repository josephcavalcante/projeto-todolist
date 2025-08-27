package modelo;

import java.io.Serializable;

// representação do usuário do sistema
// email é fixo e imutável
public class Usuario implements Serializable {
    private String nomeUsuario;
    private final String emailFixo; // não pode ser alterado

    // construção do objeto usuário
    public Usuario(String nome, String email) {
        this.nomeUsuario = nome;
        this.emailFixo = email;
    }

    // obtenção do nome do usuário
    public String getNome() {
        return nomeUsuario;
    }

    // alteração do nome do usuário
    public void setNome(String nome) {
        this.nomeUsuario = nome;
    }

    // acesso ao email (somente leitura)
    public String getEmail() {
        return emailFixo;
    }
    
    // sem setEmail() - email é imutável
}