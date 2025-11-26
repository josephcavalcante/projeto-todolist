package interfaces.persistence;

/**
 * Interface para operações de persistência.
 * <p>
 * Define contrato para salvar e carregar qualquer tipo de objeto,
 * seguindo o princípio DIP e permitindo diferentes implementações.
 * Suporta generics para type safety.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 3.0
 * @since 2.0
 */
public interface IPersistencia {
    
    /**
     * Salva um objeto no meio de persistência.
     * 
     * @param objeto objeto a ser salvo
     * @param identificador nome do arquivo ou identificador
     * @return true se salvo com sucesso
     */
    boolean salvar(Object objeto, String identificador);
    
    /**
     * Carrega um objeto do meio de persistência.
     * 
     * @param <T> tipo do objeto a ser carregado
     * @param identificador nome do arquivo ou identificador
     * @param tipo classe do tipo esperado
     * @return objeto carregado ou null se não encontrado
     */
    <T> T carregar(String identificador, Class<T> tipo);
}