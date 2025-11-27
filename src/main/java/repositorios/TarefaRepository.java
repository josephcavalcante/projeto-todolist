package repositorios;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;
import persistencia.DatabaseManager;
import interfaces.repositories.ITarefaRepository;
import modelo.Tarefa;

/**
 * Implementação do repositório de tarefas usando arquivo.
 * <p>
 * Fornece acesso aos dados de tarefas através do ManipuladorDeTarefas,
 * seguindo o padrão Repository. Pode ser substituída por implementações
 * que usam banco de dados ou APIs sem afetar o código cliente.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 1.1
 */
public class TarefaRepository implements ITarefaRepository {

    public TarefaRepository() {
    }

    private EntityManager getEntityManager() {
        return DatabaseManager.getInstance().getEntityManager();
    }

    @Override
    public void salvar(Tarefa tarefa) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            if (tarefa.getId() == null) {
                em.persist(tarefa);
            } else {
                em.merge(tarefa);
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
    public void remover(Tarefa tarefa) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Tarefa t = em.find(Tarefa.class, tarefa.getId());
            if (t != null) {
                em.remove(t);
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
    public void atualizar(Tarefa antiga, Tarefa nova) {
        salvar(nova); // Em JPA, merge resolve atualização
    }

    @Override
    public List<Tarefa> listarTodas() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tarefa t", Tarefa.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Tarefa> listarPorData(LocalDate data) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tarefa t WHERE t.deadline = :data", Tarefa.class)
                    .setParameter("data", data)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Tarefa buscarPorTitulo(String titulo) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tarefa t WHERE t.titulo = :titulo", Tarefa.class)
                    .setParameter("titulo", titulo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}