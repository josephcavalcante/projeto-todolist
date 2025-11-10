package controle.services;

import interfaces.IUsuarioService;
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
    private boolean logado = false; // controle de sessão
    
    /**
     * Construtor padrão que cria um usuário com dados padrão.
     */
    public UsuarioService() {
        this.usuario = new Usuario("Usuário", "projetopoo00@gmail.com");
    }
    
    /**
     * Construtor que inicializa com um usuário existente.
     * 
     * @param usuario usuário a ser gerenciado pelo service
     */
    public UsuarioService(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Usuario obterUsuario() {
        return usuario;
    }
    
    public void alterarNome(String novoNome) {
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            usuario.setNome(novoNome.trim());
        }
    }
    
    public String obterEmail() {
        return usuario.getEmail();
    }
    
    @Override
    public void definirSenha(String senha) {
        if (senha != null && !senha.trim().isEmpty()) {
            usuario.setSenha(senha.trim());
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