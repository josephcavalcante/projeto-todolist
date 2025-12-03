package interfaces.observer;

/**
 * Interface Observer para o padrão Observer.
 * <p>
 * Define o contrato para objetos que desejam ser notificados
 * sobre mudanças no estado de um Subject.
 * </p>
 */
public interface IObserver {
    /**
     * Método chamado quando o Subject sofre uma alteração.
     * 
     * @param mensagem mensagem ou objeto descrevendo a mudança
     */
    void atualizar(Object mensagem);
}
