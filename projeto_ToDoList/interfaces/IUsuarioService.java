package interfaces;

import modelo.Usuario;

/**
 * Interface para serviços de gestão de usuário.
 * <p>
 * Define operações relacionadas ao gerenciamento de dados do usuário,
 * seguindo o princípio ISP (Interface Segregation Principle).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public interface IUsuarioService {
    /**
     * Obtém o usuário atual do sistema.
     * 
     * @return o usuário atual
     */
    Usuario obterUsuario();
    
    /**
     * Altera o nome do usuário atual.
     * 
     * @param novoNome novo nome para o usuário
     */
    void alterarNome(String novoNome);
    
    /**
     * Obtém o email do usuário atual.
     * 
     * @return email do usuário
     */
    String obterEmail();
    
    /**
     * Define a senha do usuário.
     * 
     * @param senha nova senha do usuário
     */
    void definirSenha(String senha);
    
    /**
     * Verifica se o usuário tem senha definida.
     * 
     * @return true se tem senha, false caso contrário
     */
    boolean temSenha();
    
    /**
     * Realiza login com senha.
     * 
     * @param senha senha fornecida pelo usuário
     * @return true se login bem-sucedido, false caso contrário
     */
    boolean login(String senha);
    
    /**
     * Realiza logout do sistema.
     */
    void logout();
    
    /**
     * Verifica se o usuário está logado.
     * 
     * @return true se está logado, false caso contrário
     */
    boolean isLogado();
}