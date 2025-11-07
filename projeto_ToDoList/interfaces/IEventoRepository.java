package interfaces;

import java.time.LocalDate;
import java.util.List;

import modelo.Evento;

/**
 * Interface para repositório de eventos.
 * <p>
 * Define operações de acesso a dados para eventos, seguindo o padrão Repository
 * e o princípio ISP (Interface Segregation Principle). Permite diferentes
 * implementações sem afetar o código cliente.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public interface IEventoRepository {
    
    /**
     * Salva um evento no repositório.
     * 
     * @param evento o evento a ser salvo
     * @throws IllegalArgumentException se o evento for nulo
     */
    void salvar(Evento evento);
    
    /**
     * Remove um evento do repositório.
     * 
     * @param evento o evento a ser removido
     */
    void remover(Evento evento);
    
    /**
     * Atualiza um evento existente.
     * 
     * @param antigo o evento a ser substituído
     * @param novo a nova versão do evento
     */
    void atualizar(Evento antigo, Evento novo);
    
    /**
     * Lista todos os eventos do repositório.
     * 
     * @return lista com todos os eventos, ou lista vazia se não houver eventos
     */
    List<Evento> listarTodos();
    
    /**
     * Lista eventos de uma data específica.
     * 
     * @param data a data para filtrar os eventos
     * @return lista de eventos da data especificada
     */
    List<Evento> listarPorData(LocalDate data);
    
    /**
     * Lista eventos de um mês específico.
     * 
     * @param mes o mês (1-12)
     * @param ano o ano
     * @return lista de eventos do mês especificado
     */
    List<Evento> listarPorMes(int mes, int ano);
    
    /**
     * Busca um evento pelo título e data.
     * 
     * @param titulo o título do evento
     * @param data a data do evento
     * @return o evento encontrado ou null se não existir
     */
    Evento buscarPorTituloEData(String titulo, LocalDate data);
    
    /**
     * Verifica se existe conflito de eventos na data especificada.
     * 
     * @param data a data a ser verificada
     * @return true se já existe evento na data, false caso contrário
     */
    boolean existeEventoNaData(LocalDate data);
}