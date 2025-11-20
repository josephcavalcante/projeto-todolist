package interfaces.persistence;

import negocio.ManipuladorDeTarefas;

/**
 * Interface para operações de persistência de dados.
 * <p>
 * Define o contrato para salvar e carregar dados, seguindo o princípio
 * DIP (Dependency Inversion Principle). Permite diferentes implementações
 * como arquivo, banco de dados, etc.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public interface IPersistencia {
    
    /**
     * Salva o manipulador de tarefas no meio de persistência.
     * 
     * @param manipulador dados a serem salvos
     * @param nomeArquivo nome do arquivo ou identificador
     */
    void salvarManipulador(ManipuladorDeTarefas manipulador, String nomeArquivo);
    
    /**
     * Carrega o manipulador de tarefas do meio de persistência.
     * 
     * @param nomeArquivo nome do arquivo ou identificador
     * @return manipulador carregado ou null se não encontrado
     */
    ManipuladorDeTarefas carregarManipulador(String nomeArquivo);
}