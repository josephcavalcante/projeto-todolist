package controle.services;

import interfaces.services.IUsuarioService;
import interfaces.repositories.IUsuarioRepository;
import modelo.Usuario;

/**
 * Service responsável pela gestão de dados do usuário.
 * <p>
 * Centraliza operações relacionadas ao usuário do sistema, aplicando
 * o princípio SRP (Single Responsibility Principle).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public class UsuarioService implements IUsuarioService {
    private Usuario usuario;
    private boolean logado = false;
    private IUsuarioRepository repositorio;

    public UsuarioService(IUsuarioRepository repositorio) {
        this.repositorio = repositorio;
        this.usuario = repositorio.carregar();
        if (this.usuario == null) {
            this.usuario = new Usuario("Usuário", "projetopoo00@gmail.com");
            repositorio.salvar(this.usuario);
        }
    }

    public Usuario obterUsuario() {
        return usuario;
    }

    public void alterarNome(String novoNome) {
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            usuario.setNome(novoNome.trim());
            repositorio.salvar(usuario);
        }
    }

    public String obterEmail() {
        return usuario.getEmail();
    }

    @Override
    public void definirSenha(String senha) {
        if (senha != null && !senha.trim().isEmpty()) {
            usuario.setSenha(senha.trim());
            repositorio.salvar(usuario);
        }
    }

    @Override
    public boolean temSenha() {
        return usuario.temSenha();
    }

    @Override
    public boolean login(String senha) {
        if (usuario.verificarSenha(senha)) {
            logado = true;
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        logado = false;
    }

    @Override
    public boolean isLogado() {
        return logado;
    }
}