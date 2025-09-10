package controllers;

import controle.ManipuladorDeTarefas;
import persistencia.Persistencia;
import interfaces.IUsuarioService;

// controller especifico pra persistencia - SRP
// so cuida de salvar e carregar dados
public class PersistenciaController {
    private Persistencia persistencia;
    private static final String ARQUIVO_DADOS = "todolist.dat";
    
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