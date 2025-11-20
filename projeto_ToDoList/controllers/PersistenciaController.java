package controllers;

import interfaces.persistence.IPersistenciaGenerica;

/**
 * Controller genérico responsável por coordenar operações de persistência.
 * <p>
 * Gerencia salvamento e carregamento de qualquer tipo de objeto,
 * aplicando SRP, DIP e OCP. Totalmente desacoplado de tipos específicos.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 3.0
 * @since 2.0
 */
public class PersistenciaController {
    private IPersistenciaGenerica persistencia;
    
    /**
     * Construtor que injeta a dependência de persistência.
     * 
     * @param persistencia implementação genérica de persistência
     */
    public PersistenciaController(IPersistenciaGenerica persistencia) {
        this.persistencia = persistencia;
    }
    
    /**
     * Coordena o salvamento de um objeto.
     * 
     * @param objeto objeto a ser salvo
     * @param identificador nome do arquivo ou identificador
     * @return true se salvo com sucesso
     */
    public boolean salvar(Object objeto, String identificador) {
        return persistencia.salvar(objeto, identificador);
    }
    
    /**
     * Coordena o carregamento de um objeto.
     * 
     * @param <T> tipo do objeto esperado
     * @param identificador nome do arquivo ou identificador
     * @param tipo classe do tipo esperado
     * @return objeto carregado ou null se não encontrado
     */
    public <T> T carregar(String identificador, Class<T> tipo) {
        return persistencia.carregar(identificador, tipo);
    }
}