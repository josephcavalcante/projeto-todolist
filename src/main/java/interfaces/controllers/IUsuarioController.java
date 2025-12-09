package interfaces.controllers;

import modelo.Usuario;

/**
 * Interface para o controlador de usuários.
 * Implementa o padrão Controller do GRASP.
 */
public interface IUsuarioController {
    Usuario obterUsuario();

    boolean login(String email, String senha);

    boolean cadastrar(String nome, String email, String senha);

    void logout();

    boolean isLogado();

    void alterarNome(String novoNome);

    String obterEmail();
}
