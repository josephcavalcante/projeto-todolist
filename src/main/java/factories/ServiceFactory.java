package factories;

import interfaces.repositories.ITarefaRepository;
import interfaces.validators.IValidadorTarefa;
import interfaces.services.ITarefaService;
import controle.ManipuladorDeTarefas;
import controle.services.EventoService;
import controle.services.SubtarefaService;
import relatorios.GeradorDeRelatorios;
import controle.services.TarefaService;
import controle.services.UsuarioService;
import interfaces.services.IRelatorioService;
import interfaces.services.IUsuarioService;
import interfaces.services.ISubtarefaService;
import interfaces.services.IEventoService;
import interfaces.repositories.IEventoRepository;
import interfaces.validators.IValidadorEvento;
import repositorios.TarefaRepository;
import repositorios.EventoRepository;
import validadores.ValidadorTarefa;
import validadores.ValidadorEvento;
import controllers.TarefaController;
import controllers.SubtarefaController;
import controllers.EventoController;
import controllers.PersistenciaController;
import interfaces.controllers.IPersistenciaController;
import interfaces.controllers.ITarefaController;
import interfaces.controllers.IEventoController;
import interfaces.controllers.ISubtarefaController;
import interfaces.persistence.IPersistencia;
import persistencia.Persistencia;

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
    
    /**
     * Cria uma instância de TarefaController com suas dependências injetadas.
     * 
     * @param manipulador manipulador de tarefas
     * @return instância configurada de ITarefaController
     */
    public static ITarefaController criarTarefaController(ManipuladorDeTarefas manipulador) {
        ITarefaService tarefaService = criarTarefaService(manipulador);
        return new TarefaController(tarefaService);
    }
    
    /**
     * Cria uma instância de SubtarefaController com suas dependências injetadas.
     * 
     * @param manipulador manipulador de tarefas
     * @param tarefaService service de tarefas
     * @return instância configurada de ISubtarefaController
     */
    public static ISubtarefaController criarSubtarefaController(ManipuladorDeTarefas manipulador, ITarefaService tarefaService) {
        ISubtarefaService subtarefaService = criarSubtarefaService(manipulador, tarefaService);
        return new SubtarefaController(subtarefaService);
    }
    
    /**
     * Cria uma instância de PersistenciaController com suas dependências injetadas.
     * 
     * @return instância configurada de IPersistenciaController
     */
    public static IPersistenciaController criarPersistenciaController() {
        IPersistencia persistencia = new Persistencia();
        return new PersistenciaController(persistencia);
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
     * @return instância de IEventoController
     */
    public static IEventoController criarEventoController() {
        IEventoService eventoService = criarEventoService();
        return new EventoController(eventoService);
    }
}