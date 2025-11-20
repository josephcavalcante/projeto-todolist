package interfaces.services;

import modelo.Evento;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para serviços de gestão de eventos.
 * <p>
 * Define operações CRUD para eventos, mantendo baixo acoplamento
 * e seguindo o princípio ISP (Interface Segregation Principle).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public interface IEventoService {
    
    /**
     * Cadastra um novo evento no sistema.
     * 
     * @param titulo título do evento (obrigatório)
     * @param descricao descrição do evento
     * @param dataEvento data do evento
     * @param local local do evento
     * @return true se o evento foi cadastrado com sucesso, false caso contrário
     */
    boolean cadastrar(String titulo, String descricao, LocalDate dataEvento, String local);
    
    /**
     * Edita um evento existente.
     * 
     * @param tituloAntigo título atual do evento
     * @param dataAntiga data atual do evento
     * @param novoTitulo novo título do evento
     * @param novaDescricao nova descrição do evento
     * @param novaData nova data do evento
     * @param novoLocal novo local do evento
     * @return true se a edição foi bem-sucedida, false caso contrário
     */
    boolean editar(String tituloAntigo, LocalDate dataAntiga, String novoTitulo, 
                  String novaDescricao, LocalDate novaData, String novoLocal);
    
    /**
     * Remove um evento do sistema.
     * 
     * @param titulo título do evento a ser removido
     * @param dataEvento data do evento a ser removido
     * @return true se a remoção foi bem-sucedida, false caso contrário
     */
    boolean remover(String titulo, LocalDate dataEvento);
    
    /**
     * Lista todos os eventos com dias restantes.
     * 
     * @return lista de eventos com informações de dias restantes
     */
    List<Evento> listarTodosComDiasRestantes();
    
    /**
     * Lista eventos de uma data específica.
     * 
     * @param data data para filtrar os eventos
     * @return lista de eventos da data especificada
     */
    List<Evento> listarPorData(LocalDate data);
    
    /**
     * Lista eventos de um mês específico.
     * 
     * @param mes mês (1-12)
     * @param ano ano
     * @return lista de eventos do mês especificado
     */
    List<Evento> listarPorMes(int mes, int ano);
    
    /**
     * Busca um evento pelo título e data.
     * 
     * @param titulo título do evento
     * @param dataEvento data do evento
     * @return o evento encontrado ou null se não existir
     */
    Evento buscarEvento(String titulo, LocalDate dataEvento);
}