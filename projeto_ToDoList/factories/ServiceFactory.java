package factories;

import controle.ManipuladorDeTarefas;
import controle.TarefaService;
import controle.SubtarefaService;
import interfaces.ITarefaRepository;
import interfaces.IValidadorTarefa;
import interfaces.IRelatorioService;
import interfaces.IUsuarioService;
import interfaces.ISubtarefaService;
import repositorios.TarefaRepository;
import validadores.ValidadorTarefa;
import servicos.RelatorioService;
import servicos.UsuarioService;
import controllers.TarefaController;
import controllers.SubtarefaController;
import controllers.PersistenciaController;

// factory pra criar os services - GRASP Creator + OCP
// facilita criacao e permite trocar implementacoes
public class ServiceFactory {
    
    public static TarefaService criarTarefaService(ManipuladorDeTarefas manipulador) {
        ITarefaRepository repositorio = new TarefaRepository(manipulador);
        IValidadorTarefa validador = new ValidadorTarefa();
        return new TarefaService(repositorio, validador);
    }
    
    public static IRelatorioService criarRelatorioService() {
        return new RelatorioService();
    }
    
    public static IUsuarioService criarUsuarioService() {
        return new UsuarioService();
    }
    
    public static ISubtarefaService criarSubtarefaService(ManipuladorDeTarefas manipulador, TarefaService tarefaService) {
        return new SubtarefaService(manipulador, tarefaService);
    }
    
    public static TarefaController criarTarefaController(ManipuladorDeTarefas manipulador) {
        TarefaService tarefaService = criarTarefaService(manipulador);
        return new TarefaController(tarefaService);
    }
    
    public static SubtarefaController criarSubtarefaController(ManipuladorDeTarefas manipulador, TarefaService tarefaService) {
        ISubtarefaService subtarefaService = criarSubtarefaService(manipulador, tarefaService);
        return new SubtarefaController(subtarefaService);
    }
    
    public static PersistenciaController criarPersistenciaController() {
        return new PersistenciaController();
    }
}