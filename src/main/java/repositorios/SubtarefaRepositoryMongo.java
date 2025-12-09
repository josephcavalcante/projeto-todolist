package repositorios;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import interfaces.repositories.ISubtarefaRepository;
import modelo.Subtarefa;
import modelo.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class SubtarefaRepositoryMongo implements ISubtarefaRepository {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public SubtarefaRepositoryMongo() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("todolist_db");
            collection = database.getCollection("subtarefas");
        } catch (Exception e) {
            System.err.println("Erro ao conectar no MongoDB: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Subtarefa subtarefa) {
        Document doc = new Document("titulo", subtarefa.getTitulo())
                .append("descricao", subtarefa.getDescricao())
                .append("percentual", subtarefa.getPercentual())
                .append("tarefaId", subtarefa.getTarefa().getId());

        // Se já existe (por título e tarefaId), atualiza
        Document existing = collection.find(Filters.and(
                Filters.eq("titulo", subtarefa.getTitulo()),
                Filters.eq("tarefaId", subtarefa.getTarefa().getId()))).first();

        if (existing != null) {
            collection.replaceOne(Filters.eq("_id", existing.get("_id")), doc);
        } else {
            collection.insertOne(doc);
        }
    }

    @Override
    public void remover(Subtarefa subtarefa) {
        collection.deleteOne(Filters.and(
                Filters.eq("titulo", subtarefa.getTitulo()),
                Filters.eq("tarefaId", subtarefa.getTarefa().getId())));
    }

    @Override
    public List<Subtarefa> listarPorTarefaId(Long tarefaId) {
        List<Subtarefa> lista = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("tarefaId", tarefaId))) {
            Subtarefa s = new Subtarefa(
                    doc.getString("titulo"),
                    doc.getString("descricao"),
                    doc.getDouble("percentual"));
            // Precisamos setar a tarefa aqui? Talvez só o ID bastasse
            // Por enquanto, cria uma Tarefa dummy só com ID para manter a referência
            Tarefa t = new Tarefa();
            t.setId(tarefaId);
            s.setTarefa(t);
            lista.add(s);
        }
        return lista;
    }

    @Override
    public Subtarefa buscarPorTitulo(String titulo, Long tarefaId) {
        Document doc = collection.find(Filters.and(
                Filters.eq("titulo", titulo),
                Filters.eq("tarefaId", tarefaId))).first();

        if (doc != null) {
            Subtarefa s = new Subtarefa(
                    doc.getString("titulo"),
                    doc.getString("descricao"),
                    doc.getDouble("percentual"));
            Tarefa t = new Tarefa();
            t.setId(tarefaId);
            s.setTarefa(t);
            return s;
        }
        return null;
    }
}
