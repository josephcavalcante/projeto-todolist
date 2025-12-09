package controllers;

import interfaces.controllers.IUsuarioController;
import interfaces.services.IUsuarioService;
import modelo.Usuario;

/**
 * Controlador responsável pelas operações de Usuário.
 * Intermedeia a comunicação entre a UI/Facade e o Service.
 */
public class UsuarioController implements IUsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Usuario obterUsuario() {
        return usuarioService.obterUsuario();
    }

    @Override
    public boolean login(String email, String senha) {
        return usuarioService.login(email, senha);
    }

    @Override
    public boolean cadastrar(String nome, String email, String senha) {
        return usuarioService.cadastrar(nome, email, senha);
    }

    @Override
    public void logout() {
        usuarioService.logout();
    }

    @Override
    public boolean isLogado() {
        return usuarioService.isLogado();
    }

    @Override
    public void alterarNome(String novoNome) {
        usuarioService.alterarNome(novoNome);
    }

    @Override
    public String obterEmail() {
        return usuarioService.obterEmail();
    }
}
