package servicos;

import modelo.Usuario;

// service pra gerenciar dados do usuario
// tirei essa responsabilidade da classe principal
public class UsuarioService {
    private Usuario usuario;
    
    public UsuarioService() {
        this.usuario = new Usuario("Usuário", "projetopoo00@gmail.com");
    }
    
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
}