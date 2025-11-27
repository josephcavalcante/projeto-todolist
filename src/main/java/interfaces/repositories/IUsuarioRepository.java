package interfaces.repositories;

import modelo.Usuario;

public interface IUsuarioRepository {
    void salvar(Usuario usuario);

    Usuario carregar();

    boolean existeUsuario();
}
