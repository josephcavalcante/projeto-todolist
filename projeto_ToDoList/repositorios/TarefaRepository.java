package repositorios;

import interfaces.ITarefaRepository;
import modelo.Tarefa;
import controle.ManipuladorDeTarefas;
import java.time.LocalDate;
import java.util.List;

// implementacao do repositorio
// aqui fica toda a logica de acesso aos dados
public class TarefaRepository implements ITarefaRepository {
    private ManipuladorDeTarefas manipulador;
    
    public TarefaRepository(ManipuladorDeTarefas manipulador) {
        this.manipulador = manipulador;
    }
    
    @Override
    public void salvar(Tarefa tarefa) {
        manipulador.adicionarTarefa(tarefa);
    }
    
    @Override
    public void remover(Tarefa tarefa) {
        manipulador.removerTarefa(tarefa);
    }
    
    @Override
    public void atualizar(Tarefa antiga, Tarefa nova) {
        manipulador.editarTarefa(antiga, nova);
    }
    
    @Override
    public List<Tarefa> listarTodas() {
        return manipulador.listarTarefas();
    }
    
    @Override
    public List<Tarefa> listarPorData(LocalDate data) {
        return manipulador.listarTarefasPorData(data);
    }
    
    @Override
    public Tarefa buscarPorTitulo(String titulo) {
        return listarTodas().stream()
            .filter(t -> t.getTitulo().equalsIgnoreCase(titulo))
            .findFirst()
            .orElse(null);
    }
}