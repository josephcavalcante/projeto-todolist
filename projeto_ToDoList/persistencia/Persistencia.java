package persistencia;

import java.io.*;

import negocio.ManipuladorDeTarefas;

// gerenciamento da persistência de dados
// usa serialização padrão do Java
public class Persistencia {

    // gravação do manipulador em arquivo
    public void salvarManipulador(ManipuladorDeTarefas manipulador, String nomeArquivo) {
        try (ObjectOutputStream gravador = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            gravador.writeObject(manipulador);
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }

    // carregamento do manipulador do arquivo
    public ManipuladorDeTarefas carregarManipulador(String nomeArquivo) {
        try (ObjectInputStream leitor = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            return (ManipuladorDeTarefas) leitor.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            return null; // arquivo inexistente ou corrompido
        }
    }
} 