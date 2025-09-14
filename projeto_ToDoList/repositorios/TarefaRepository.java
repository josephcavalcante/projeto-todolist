package repositorios;

import java.time.LocalDate;
import java.util.List;

import controle.ManipuladorDeTarefas;
import interfaces.ITarefaRepository;
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
    private ManipuladorDeTarefas manipulador;
    
    /**
     * Construtor que injeta o manipulador de tarefas.
     * 
     * @param manipulador manipulador responsável pelo acesso aos dados
     */
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