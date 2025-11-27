package persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton para gerenciar a conexão com o banco de dados (JPA).
 * <p>
 * Garante que apenas uma instância do EntityManagerFactory seja criada
 * durante o ciclo de vida da aplicação.
 * </p>
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private EntityManagerFactory emf;

    private DatabaseManager() {
        try {
            this.emf = Persistence.createEntityManagerFactory("todoListPU");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar JPA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
