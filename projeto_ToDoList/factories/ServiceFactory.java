package factories;

import interfaces.ITarefaRepository;
import interfaces.IValidadorTarefa;
import interfaces.ITarefaService;
import controle.ManipuladorDeTarefas;
import controle.services.EventoService;
import controle.services.SubtarefaService;
import relatorios.GeradorDeRelatorios;
import controle.services.TarefaService;
import controle.services.UsuarioService;
import interfaces.IRelatorioService;
import interfaces.IUsuarioService;
import interfaces.ISubtarefaService;
import interfaces.IEventoService;
import interfaces.IEventoRepository;
import interfaces.IValidadorEvento;
import repositorios.TarefaRepository;
import repositorios.EventoRepository;
import validadores.ValidadorTarefa;
import validadores.ValidadorEvento;
import controllers.TarefaController;
import controllers.SubtarefaController;
import controllers.EventoController;
import controllers.PersistenciaController;

/**
 * Factory responsável pela criação e configuração de services e controllers.
 * <p>
 * Centraliza a criação de objetos e injeção de dependências, seguindo os padrões
 * GRASP Creator e Factory Method. Facilita a manutenção e permite trocar
 * implementações sem afetar o código cliente (OCP).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public class ServiceFactory {
    
    /**
     * Cria uma instância de TarefaService com suas dependências injetadas.
     * 
     * @param manipulador manipulador de tarefas para o repositório
     * @return instância configurada de ITarefaService
     */
    public static ITarefaService criarTarefaService(ManipuladorDeTarefas manipulador) {
        ITarefaRepository repositorio = new TarefaRepository(manipulador);
        IValidadorTarefa validador = new ValidadorTarefa();
        return new TarefaService(repositorio, validador);
    }
    
    /**
     * Cria uma instância de GeradorDeRelatorios.
     * 
     * @return instância de IRelatorioService
     */
    public static IRelatorioService criarRelatorioService() {
        return new GeradorDeRelatorios();
    }
    
    public static IUsuarioService criarUsuarioService() {
        return new UsuarioService();
    }
    
    public static ISubtarefaService criarSubtarefaService(ManipuladorDeTarefas manipulador, ITarefaService tarefaService) {
        return new SubtarefaService(manipulador, tarefaService);
    }
    
    public static TarefaController criarTarefaController(ManipuladorDeTarefas manipulador) {
        ITarefaService tarefaService = criarTarefaService(manipulador);
        return new TarefaController(tarefaService);
    }
    
    public static SubtarefaController criarSubtarefaController(ManipuladorDeTarefas manipulador, ITarefaService tarefaService) {
        ISubtarefaService subtarefaService = criarSubtarefaService(manipulador, tarefaService);
        return new SubtarefaController(subtarefaService);
    }
    
    public static PersistenciaController criarPersistenciaController() {
        return new PersistenciaController();
    }
    
    /**
     * Cria uma instância de EventoService com suas dependências injetadas.
     * 
     * @return instância configurada de EventoService
     */
    public static IEventoService criarEventoService() {
        IEventoRepository repositorio = new EventoRepository();
        IValidadorEvento validador = new ValidadorEvento();
        return new EventoService(repositorio, validador);
    }
    
    /**
     * Cria uma instância de EventoController.
     * 
     * @return instância de EventoController
     */
    public static EventoController criarEventoController() {
        IEventoService eventoService = criarEventoService();
        return new EventoController(eventoService);
    }
}