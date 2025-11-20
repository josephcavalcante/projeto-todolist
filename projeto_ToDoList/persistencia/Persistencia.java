package persistencia;

import java.io.*;
import interfaces.persistence.IPersistencia;
import interfaces.persistence.IPersistenciaGenerica;
import negocio.ManipuladorDeTarefas;

/**
 * Implementação de persistência usando serialização Java.
 * <p>
 * Gerencia o salvamento e carregamento de dados em arquivo,
 * seguindo o padrão Repository e implementando ambas as interfaces
 * de persistência (específica e genérica).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 1.0
 */
public class Persistencia implements IPersistencia, IPersistenciaGenerica {

    /**
     * Salva o manipulador em arquivo usando serialização Java.
     * 
     * @param manipulador dados a serem salvos
     * @param nomeArquivo nome do arquivo de destino
     */
    @Override
    public void salvarManipulador(ManipuladorDeTarefas manipulador, String nomeArquivo) {
        try (ObjectOutputStream gravador = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            gravador.writeObject(manipulador);
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }

    /**
     * Carrega o manipulador do arquivo usando desserialização Java.
     * 
     * @param nomeArquivo nome do arquivo de origem
     * @return manipulador carregado ou null se arquivo não existir
     */
    @Override
    public ManipuladorDeTarefas carregarManipulador(String nomeArquivo) {
        try (ObjectInputStream leitor = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            return (ManipuladorDeTarefas) leitor.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            return null; // arquivo inexistente ou corrompido
        }
    }
    
    /**
     * Implementação genérica do salvamento.
     * 
     * @param objeto objeto a ser salvo
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
     * 
     * @param <T> tipo do objeto esperado
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