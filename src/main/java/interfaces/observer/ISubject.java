package interfaces.observer;

/**
 * Interface Subject para o padrão Observer.
 * <p>
 * Define o contrato para objetos que são observados.
 * Permite registrar, remover e notificar observadores.
 * </p>
 */
public interface ISubject {
    /**
     * Registra um novo observador.
     * 
     * @param observer observador a ser registrado
     */
    void adicionarObservador(IObserver observer);

    /**
     * Remove um observador existente.
     * 
     * @param observer observador a ser removido
     */
    void removerObservador(IObserver observer);

    /**
     * Notifica todos os observadores registrados sobre uma mudança.
     * 
     * @param mensagem mensagem ou objeto descrevendo a mudança
     */
    void notificarObservadores(Object mensagem);
}
