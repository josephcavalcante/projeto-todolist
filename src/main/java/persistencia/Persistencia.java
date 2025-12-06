package persistencia;

import java.io.*;
import interfaces.persistence.IPersistencia;
// Import removido pois a classe não existe mais

/**
 * Implementação de persistência usando serialização Java.
 * <p>
 * Gerencia o salvamento e carregamento de dados em arquivo,
 * seguindo o padrão Repository e implementando IPersistenciaGenerica.
 * Suporta qualquer tipo de objeto via generics.
 * </p>
 * * @author Projeto ToDoList
 * @version 3.0
 * @since 1.0
 */
public class Persistencia implements IPersistencia {

    /**
     * Implementação genérica do salvamento.
     * * @param objeto objeto a ser salvo
     * @param identificador nome do arquivo
     * @return true se salvo com sucesso
     */
    @Override
    public boolean salvar(Object objeto, String identificador) {
        try (ObjectOutputStream gravador = new ObjectOutputStream(new FileOutputStream(identificador))) {
            gravador.writeObject(objeto);
            return true;
        } catch (IOException erro) {
            return false;
        }
    }
    
    /**
     * Implementação genérica do carregamento.
     * * @param <T> tipo do objeto esperado
     * @param identificador nome do arquivo
     * @param tipo classe do tipo esperado
     * @return objeto carregado ou null se não encontrado
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T carregar(String identificador, Class<T> tipo) {
        try (ObjectInputStream leitor = new ObjectInputStream(new FileInputStream(identificador))) {
            Object objeto = leitor.readObject();
            if (tipo.isInstance(objeto)) {
                return (T) objeto;
            }
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }
}
    

