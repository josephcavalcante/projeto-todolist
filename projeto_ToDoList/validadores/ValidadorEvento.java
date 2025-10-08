package validadores;

import interfaces.IValidadorEvento;
import interfaces.IEventoRepository;
import modelo.Evento;
import java.time.LocalDate;

/**
 * Implementação do validador de eventos.
 * <p>
 * Fornece validações para eventos, incluindo verificação de conflitos
 * de datas. Segue o princípio SRP (Single Responsibility Principle).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public class ValidadorEvento implements IValidadorEvento {
    
    @Override
    public boolean validarTitulo(String titulo) {
        return titulo != null && !titulo.trim().isEmpty() && titulo.trim().length() >= 3;
    }
    
    @Override
    public boolean validarData(LocalDate dataEvento) {
        return dataEvento != null && !dataEvento.isBefore(LocalDate.now());
    }
    
    @Override
    public boolean validarLocal(String local) {
        return local != null && !local.trim().isEmpty();
    }
    
    @Override
    public boolean validarEvento(Evento evento) {
        return evento != null && 
               validarTitulo(evento.getTitulo()) && 
               validarData(evento.getDataEvento()) &&
               validarLocal(evento.getLocal());
    }
    
    @Override
    public boolean validarSemConflito(LocalDate dataEvento, IEventoRepository eventoRepository) {
        return !eventoRepository.existeEventoNaData(dataEvento);
    }
}