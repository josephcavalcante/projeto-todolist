package interfaces.validators;

import modelo.Evento;
import java.time.LocalDate;

/**
 * Interface para validadores de evento.
 * <p>
 * Define operações de validação de eventos, permitindo diferentes
 * implementações seguindo o padrão Strategy e o princípio ISP.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public interface IValidadorEvento {
    
    /**
     * Valida o título de um evento.
     * 
     * @param titulo título a ser validado
     * @return true se o título é válido, false caso contrário
     */
    boolean validarTitulo(String titulo);
    
    /**
     * Valida a data de um evento.
     * 
     * @param dataEvento data a ser validada
     * @return true se a data é válida, false caso contrário
     */
    boolean validarData(LocalDate dataEvento);
    
    /**
     * Valida o local de um evento.
     * 
     * @param local local a ser validado
     * @return true se o local é válido, false caso contrário
     */
    boolean validarLocal(String local);
    
    /**
     * Valida um evento completo.
     * 
     * @param evento evento a ser validado
     * @return true se o evento é válido, false caso contrário
     */
    boolean validarEvento(Evento evento);
    
    /**
     * Valida se não há conflito de eventos na data.
     * 
     * @param dataEvento data a ser verificada
     * @param eventoRepository repositório para verificar conflitos
     * @return true se não há conflito, false se já existe evento na data
     */
    boolean validarSemConflito(LocalDate dataEvento, interfaces.repositories.IEventoRepository eventoRepository);
}