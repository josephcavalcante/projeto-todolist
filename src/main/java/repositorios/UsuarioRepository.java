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
            if (usuario.getId() == null)
                em.persist(usuario);
            else
                em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.emailFixo = :email", Usuario.class)
                    .setParameter("email", email).getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario carregar() {
        return null;
    }

    @Override
    public boolean existeUsuario() {
        return false;
    }
}