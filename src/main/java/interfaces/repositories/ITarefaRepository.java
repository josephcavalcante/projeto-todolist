package interfaces.repositories;

import modelo.Tarefa;
import modelo.Usuario;
import java.time.LocalDate;
import java.util.List;


 /*
  *Define as operações de acesso a dados para tarefas, seguindo o padrão
 * Repository.
 * Permite diferentes implementações (arquivo, banco de dados, API) sem afetar o
 * código cliente.*/

public interface ITarefaRepository {
    void salvar(Tarefa tarefa);
    void remover(Tarefa tarefa);
    void atualizar(Tarefa antiga, Tarefa nova);
    Tarefa buscarPorTitulo(String titulo);
    Tarefa buscarPorId(Long id);
    
    // --- Métodos Filtrados por Usuário ---
    List<Tarefa> listarPorUsuario(Usuario usuario);
    List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario);
    
    // Métodos legados
    List<Tarefa> listarTodas(); 
    List<Tarefa> listarPorData(LocalDate data);
}