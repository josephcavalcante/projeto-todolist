package interfaces;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para repositório de tarefas.
 * <p>
 * Define as operações de acesso a dados para tarefas, seguindo o padrão Repository.
 * Permite diferentes implementações (arquivo, banco de dados, API) sem afetar o código cliente.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 1.1
 */
public interface ITarefaRepository {
    /**
     * Salva uma tarefa no repositório.
     * 
     * @param tarefa a tarefa a ser salva
     * @throws IllegalArgumentException se a tarefa for nula
     */
    void salvar(Tarefa tarefa);
    
    /**
     * Remove uma tarefa do repositório.
     * 
     * @param tarefa a tarefa a ser removida
     */
    void remover(Tarefa tarefa);
    
    /**
     * Atualiza uma tarefa existente.
     * 
     * @param antiga a tarefa a ser substituída
     * @param nova a nova versão da tarefa
     */
    void atualizar(Tarefa antiga, Tarefa nova);
    
    /**
     * Lista todas as tarefas do repositório.
     * 
     * @return lista com todas as tarefas, ou lista vazia se não houver tarefas
     */
    List<Tarefa> listarTodas();
    
    /**
     * Lista tarefas filtradas por data específica.
     * 
     * @param data a data para filtrar as tarefas
     * @return lista de tarefas da data especificada
     */
    List<Tarefa> listarPorData(LocalDate data);
    
    /**
     * Busca uma tarefa pelo título.
     * 
     * @param titulo o título da tarefa a ser buscada
     * @return a tarefa encontrada ou null se não existir
     */
    Tarefa buscarPorTitulo(String titulo);
}