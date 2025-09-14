package interfaces;

import modelo.Tarefa;
import modelo.Subtarefa;
import java.util.List;

// interface pro service de subtarefas - ISP
public interface ISubtarefaService {
    boolean adicionar(String tituloTarefa, String titulo, String descricao, double percentual);
    boolean editar(String tituloTarefa, String tituloAntigo, String novoTitulo, String novaDescricao, double novoPercentual);
    boolean remover(String tituloTarefa, String titulo);
    List<Subtarefa> listar(String tituloTarefa);
}