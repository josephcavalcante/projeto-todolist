package interfaces.controllers;

import modelo.Evento;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para controller de eventos.
 * <p>
 * Define operações de coordenação para gestão de eventos,
 * seguindo o padrão GRASP Controller e o princípio DIP.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public interface IEventoController {
    
    /**
     * Cadastra um novo evento.
     * 
     * @param titulo título do evento
     * @param descricao descrição do evento
     * @param dataEvento data do evento
     * @param local local do evento
     * @return true se cadastrado com sucesso, false caso contrário
     */
    boolean cadastrarEvento(String titulo, String descricao, LocalDate dataEvento, String local);
    
    /**
     * Edita um evento existente.
     * 
     * @param tituloAntigo título atual do evento
     * @param dataAntiga data atual do evento
     * @param novoTitulo novo título
     * @param novaDescricao nova descrição
     * @param novaData nova data
     * @param novoLocal novo local
     * @return true se editado com sucesso, false caso contrário
     */
    boolean editarEvento(String tituloAntigo, LocalDate dataAntiga, String novoTitulo, 
                        String novaDescricao, LocalDate novaData, String novoLocal);
    
    /**
     * Remove um evento.
     * 
     * @param titulo título do evento
     * @param dataEvento data do evento
     * @return true se removido com sucesso, false caso contrário
     */
    boolean removerEvento(String titulo, LocalDate dataEvento);
    
    /**
     * Lista todos os eventos com dias restantes.
     * 
     * @return lista de eventos
     */
    List<Evento> listarEventosComDiasRestantes();
    
    /**
     * Lista eventos de uma data específica.
     * 
     * @param data data para filtrar
     * @return lista de eventos da data
     */
    List<Evento> listarEventosPorData(LocalDate data);
    
    /**
     * Lista eventos de um mês específico.
     * 
     * @param mes mês (1-12)
     * @param ano ano
     * @return lista de eventos do mês
     */
    List<Evento> listarEventosPorMes(int mes, int ano);
    
    /**
     * Busca um evento específico.
     * 
     * @param titulo título do evento
     * @param dataEvento data do evento
     * @return o evento encontrado ou null
     */
    Evento buscarEvento(String titulo, LocalDate dataEvento);
}