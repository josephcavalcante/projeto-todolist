package factories;

import interfaces.repositories.ITarefaRepository;
import interfaces.repositories.ISubtarefaRepository;
import interfaces.validators.IValidadorTarefa;
import interfaces.services.ITarefaService;

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
 * Centraliza a criação de objetos e injeção de dependências, seguindo os
 * padrões
 * GRASP Creator e Factory Method. Facilita a manutenção e permite trocar
 * implementações sem afetar o código cliente (OCP).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public class ServiceFactory {

    public static ITarefaService criarTarefaService() {
        ITarefaRepository repositorio = new TarefaRepository();
        IValidadorTarefa validador = new ValidadorTarefa();
        return new TarefaService(repositorio, validador);
    }

    public static IRelatorioService criarRelatorioService() {
        return new GeradorDeRelatorios();
    }

    public static IUsuarioService criarUsuarioService() {
        return new UsuarioService(new repositorios.UsuarioRepository());
    }

    public static ISubtarefaService criarSubtarefaService(ITarefaService tarefaService) {
        ISubtarefaRepository repositorio = new repositorios.SubtarefaRepositoryMongo();
        return new SubtarefaService(repositorio, tarefaService);
    }

    public static ITarefaController criarTarefaController() {
        ITarefaService tarefaService = criarTarefaService();
        return new TarefaController(tarefaService);
    }

    public static ISubtarefaController criarSubtarefaController(ITarefaService tarefaService) {
        ISubtarefaService subtarefaService = criarSubtarefaService(tarefaService);
        return new SubtarefaController(subtarefaService);
    }

    public static IPersistenciaController criarPersistenciaController() {
        IPersistencia persistencia = new Persistencia();
        return new PersistenciaController(persistencia);
    }

    public static IEventoService criarEventoService() {
        IEventoRepository repositorio = new EventoRepository();
        IValidadorEvento validador = new ValidadorEvento();
        return new EventoService(repositorio, validador);
    }

    public static IEventoController criarEventoController() {
        IEventoService eventoService = criarEventoService();
        return new EventoController(eventoService);
    }
}