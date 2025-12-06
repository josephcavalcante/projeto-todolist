package factories;

import interfaces.repositories.ITarefaRepository;
import interfaces.repositories.ISubtarefaRepository;
import interfaces.repositories.IEventoRepository;
import interfaces.validators.IValidadorTarefa;
import interfaces.validators.IValidadorEvento;
import interfaces.services.*;
import interfaces.controllers.*;
import controle.services.*;
import controllers.*;
import repositorios.*;
import validadores.*;
import persistencia.Persistencia;
import relatorios.GeradorDeRelatorios;

public class ServiceFactory {

    public static ITarefaService criarTarefaService() {
        ITarefaRepository repositorio = new TarefaRepository();
        IValidadorTarefa validador = new ValidadorTarefa();
        // --- CORREÇÃO: Cria e injeta o CacheRepository ---
        TarefaCacheRepository cacheRepository = new TarefaCacheRepository();
        return new TarefaService(repositorio, validador, cacheRepository);
        // -------------------------------------------------
    }

    public static IRelatorioService criarRelatorioService() {
        return new GeradorDeRelatorios();
    }

    public static IUsuarioService criarUsuarioService() {
        // Injeta repositórios de Usuario e Tarefa (para o cache strategy)
        return new UsuarioService(new repositorios.UsuarioRepository(), new repositorios.TarefaRepository());
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
        return new PersistenciaController(new Persistencia());
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