package interfaces;

import modelo.Usuario;

// interface pro service de usuario - ISP
public interface IUsuarioService {
    Usuario obterUsuario();
    void alterarNome(String novoNome);
    String obterEmail();
}