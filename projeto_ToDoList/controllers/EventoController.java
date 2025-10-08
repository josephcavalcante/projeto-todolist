package controllers;

import interfaces.IEventoService;
import modelo.Evento;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsável por coordenar operações relacionadas a eventos.
 * <p>
 * Atua como camada de coordenação entre a interface do usuário e o EventoService,
 * seguindo o padrão GRASP Controller. Mantém baixo acoplamento delegando
 * a lógica de negócio para o service.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public class EventoController {
    private IEventoService eventoService;
    
    /**
     * Construtor que injeta a dependência do service.
     * 
     * @param eventoService service de eventos
     */
    public EventoController(IEventoService eventoService) {
        this.eventoService = eventoService;
    }
    
    /**
     * Cadastra um novo evento.
     * 
     * @param titulo título do evento
     * @param descricao descrição do evento
     * @param dataEvento data do evento
     * @param local local do evento
     * @return true se cadastrado com sucesso, false caso contrário
     */
    public boolean cadastrarEvento(String titulo, String descricao, LocalDate dataEvento, String local) {
        return eventoService.cadastrar(titulo, descricao, dataEvento, local);
    }
    
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
    public boolean editarEvento(String tituloAntigo, LocalDate dataAntiga, String novoTitulo, 
                               String novaDescricao, LocalDate novaData, String novoLocal) {
        return eventoService.editar(tituloAntigo, dataAntiga, novoTitulo, novaDescricao, novaData, novoLocal);
    }
    
    /**
     * Remove um evento.
     * 
     * @param titulo título do evento
     * @param dataEvento data do evento
     * @return true se removido com sucesso, false caso contrário
     */
    public boolean removerEvento(String titulo, LocalDate dataEvento) {
        return eventoService.remover(titulo, dataEvento);
    }
    
    /**
     * Lista todos os eventos com dias restantes.
     * 
     * @return lista de eventos
     */
    public List<Evento> listarEventosComDiasRestantes() {
        return eventoService.listarTodosComDiasRestantes();
    }
    
    /**
     * Lista eventos de uma data específica.
     * 
     * @param data data para filtrar
     * @return lista de eventos da data
     */
    public List<Evento> listarEventosPorData(LocalDate data) {
        return eventoService.listarPorData(data);
    }
    
    /**
     * Lista eventos de um mês específico.
     * 
     * @param mes mês (1-12)
     * @param ano ano
     * @return lista de eventos do mês
     */
    public List<Evento> listarEventosPorMes(int mes, int ano) {
        return eventoService.listarPorMes(mes, ano);
    }
    
    /**
     * Busca um evento específico.
     * 
     * @param titulo título do evento
     * @param dataEvento data do evento
     * @return o evento encontrado ou null
     */
    public Evento buscarEvento(String titulo, LocalDate dataEvento) {
        return eventoService.buscarEvento(titulo, dataEvento);
    }
}