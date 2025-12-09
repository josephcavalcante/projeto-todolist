package persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseManager {
    private static DatabaseManager instance;
    private EntityManagerFactory emf;

    private DatabaseManager() {
        try {
            this.emf = Persistence.createEntityManagerFactory("todoListPU");
        } catch (Exception e) {
            System.err.println("FATAL: Erro ao conectar no Banco SQL (Porta 5433, 5432).");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        if (emf != null)
            emf.close();
    }
}