package repositorios;

import interfaces.repositories.IEventoRepository;
import modelo.Evento;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Implementação do repositório de eventos usando lista em memória.
 * <p>
 * Fornece acesso aos dados de eventos, seguindo o padrão Repository.
 * Pode ser substituída por implementações que usam banco de dados
 * sem afetar o código cliente (OCP).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public class EventoRepository implements IEventoRepository {
    private List<Evento> eventos;
    
    /**
     * Construtor que inicializa a lista de eventos.
     */
    public EventoRepository() {
        this.eventos = new ArrayList<>();
    }
    
    @Override
    public void salvar(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo");
        }
        eventos.add(evento);
    }
    
    @Override
    public void remover(Evento evento) {
        eventos.remove(evento);
    }
    
    @Override
    public void atualizar(Evento antigo, Evento novo) {
        int index = eventos.indexOf(antigo);
        if (index != -1) {
            eventos.set(index, novo);
        }
    }
    
    @Override
    public List<Evento> listarTodos() {
        return new ArrayList<>(eventos);
    }
    
    @Override
    public List<Evento> listarPorData(LocalDate data) {
        return eventos.stream()
                .filter(evento -> evento.getDataEvento().equals(data))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Evento> listarPorMes(int mes, int ano) {
        return eventos.stream()
                .filter(evento -> evento.getDataEvento().getMonthValue() == mes && 
                                evento.getDataEvento().getYear() == ano)
                .collect(Collectors.toList());
    }
    
    @Override
    public Evento buscarPorTituloEData(String titulo, LocalDate data) {
        return eventos.stream()
                .filter(evento -> evento.getTitulo().equalsIgnoreCase(titulo) && 
                                evento.getDataEvento().equals(data))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public boolean existeEventoNaData(LocalDate data) {
        return eventos.stream()
                .anyMatch(evento -> evento.getDataEvento().equals(data));
    }
}