package repositorios;

import jakarta.persistence.EntityManager;
import persistencia.DatabaseManager;
import interfaces.repositories.IUsuarioRepository;
import modelo.Usuario;
import java.util.List;

public class UsuarioRepository implements IUsuarioRepository {

    private EntityManager getEntityManager() {
        return DatabaseManager.getInstance().getEntityManager();
    }

    @Override
    public void salvar(Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // Como só existe um usuário no sistema (por enquanto), verificamos se já existe
            List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
            if (usuarios.isEmpty()) {
                em.persist(usuario);
            } else {
                // Atualiza o primeiro encontrado (assumindo single user por enquanto)
                Usuario existente = usuarios.get(0);
                existente.setNome(usuario.getNome());

                if (usuario.temSenha()) {
                    existente.setSenha(usuario.getSenha()); // Deveria ser hash, mas por enquanto mantemos
                                                            // compatibilidade
                }
                em.merge(existente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario carregar() {
        EntityManager em = getEntityManager();
        try {
            List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
            if (!usuarios.isEmpty()) {
                return usuarios.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existeUsuario() {
        return carregar() != null;
    }
}
