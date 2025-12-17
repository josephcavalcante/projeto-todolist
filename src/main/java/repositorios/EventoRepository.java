package repositorios;

import jakarta.persistence.EntityManager;
import persistencia.DatabaseManager;
import interfaces.repositories.IEventoRepository;
import modelo.Evento;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementação do repositório de eventos usando lista em memória.
 * <p>
 * Fornece acesso aos dados de eventos, seguindo o padrão Repository.
 * Pode ser substituída por implementações que usam banco de dados
 * sem afetar o código cliente (OCP).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public class EventoRepository implements IEventoRepository {

    public EventoRepository() {
    }

    private EntityManager getEntityManager() {
        return DatabaseManager.getInstance().getEntityManager();
    }

    @Override
    public void salvar(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo");
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            if (evento.getId() == null) {
                em.persist(evento);
            } else {
                em.merge(evento);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void remover(Evento evento) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Evento e = em.find(Evento.class, evento.getId());
            if (e != null) {
                em.remove(e);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizar(Evento antigo, Evento novo) {
        salvar(novo);
    }

    @Override
    public List<Evento> listarTodos() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Evento e", Evento.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> listarPorData(LocalDate data) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Evento e WHERE e.dataEvento = :data", Evento.class)
                    .setParameter("data", data)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> listarPorMes(int mes, int ano) {
        EntityManager em = getEntityManager();
        try {
            return em
                    .createQuery(
                            "SELECT e FROM Evento e WHERE MONTH(e.dataEvento) = :mes AND YEAR(e.dataEvento) = :ano",
                            Evento.class)
                    .setParameter("mes", mes)
                    .setParameter("ano", ano)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Evento buscarPorTituloEData(String titulo, LocalDate data) {
        EntityManager em = getEntityManager();
        try {
            return em
                    .createQuery("SELECT e FROM Evento e WHERE e.titulo = :titulo AND e.dataEvento = :data",
                            Evento.class)
                    .setParameter("titulo", titulo)
                    .setParameter("data", data)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existeEventoNaData(LocalDate data) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(e) FROM Evento e WHERE e.dataEvento = :data", Long.class)
                    .setParameter("data", data)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}