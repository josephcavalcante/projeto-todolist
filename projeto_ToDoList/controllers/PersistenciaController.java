package controllers;

import controle.ManipuladorDeTarefas;
import persistencia.Persistencia;
import interfaces.IUsuarioService;

/**
 * Controller responsável por coordenar operações de persistência.
 * <p>
 * Gerencia o carregamento e salvamento de dados, aplicando o princípio
 * SRP (Single Responsibility Principle) e mantendo baixo acoplamento.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public class PersistenciaController {
    private Persistencia persistencia;
    private static final String ARQUIVO_DADOS = "todolist.dat";
    
    /**
     * Construtor que inicializa o mecanismo de persistência.
     */
    public PersistenciaController() {
        this.persistencia = new Persistencia();
    }
    
    public ManipuladorDeTarefas carregarDados() {
        try {
            ManipuladorDeTarefas dados = persistencia.carregarManipulador(ARQUIVO_DADOS);
            return dados != null ? dados : new ManipuladorDeTarefas();
        } catch (Exception erro) {
            System.out.println("Arquivo não encontrado. Iniciando vazio.");
            return new ManipuladorDeTarefas();
        }
    }
    
    public boolean salvarDados(ManipuladorDeTarefas dados, IUsuarioService usuarioService) {
        try {
            dados.setUsuario(usuarioService.obterUsuario());
            persistencia.salvarManipulador(dados, ARQUIVO_DADOS);
            return true;
        } catch (Exception ex) {
            System.out.println("Erro ao salvar: " + ex.getMessage());
            return false;
        }
    }
}