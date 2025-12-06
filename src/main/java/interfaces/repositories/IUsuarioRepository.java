package interfaces.repositories;
import modelo.Usuario;

public interface IUsuarioRepository {
    void salvar(Usuario usuario);
    Usuario carregar(); // Legado
    Usuario buscarPorEmail(String email); // Novo
    boolean existeUsuario();
}