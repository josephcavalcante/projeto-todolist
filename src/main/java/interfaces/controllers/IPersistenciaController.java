package interfaces.controllers;

/**
 * Interface para controller de persistência.
 * <p>
 * Define operações de coordenação para salvamento e carregamento
 * de objetos, seguindo o princípio DIP e permitindo diferentes
 * implementações de controllers.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 3.0
 * @since 3.0
 */
public interface IPersistenciaController {
    
    /**
     * Coordena o salvamento de um objeto.
     * 
     * @param objeto objeto a ser salvo
     * @param identificador nome do arquivo ou identificador
     * @return true se salvo com sucesso
     */
    boolean salvar(Object objeto, String identificador);
    
    /**
     * Coordena o carregamento de um objeto.
     * 
     * @param <T> tipo do objeto esperado
     * @param identificador nome do arquivo ou identificador
     * @param tipo classe do tipo esperado
     * @return objeto carregado ou null se não encontrado
     */
    <T> T carregar(String identificador, Class<T> tipo);
}