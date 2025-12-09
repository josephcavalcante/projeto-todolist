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
import relatorios.GeradorDeRelatorios;

public class ServiceFactory {

    public static ITarefaService criarTarefaService() {
        // 1. Cria os componentes crus
        ITarefaRepository sqlRepo = new TarefaRepository();
        TarefaCacheRepository redisRepo = new TarefaCacheRepository();

        // 2. Envolve o SQL com o Proxy de Cache
        ITarefaRepository repoComCache = new TarefaRepositoryProxy(sqlRepo, redisRepo);

        IValidadorTarefa validador = new ValidadorTarefa();

        // 3. Entrega o Proxy para o Service (O Service nem sabe que existe cache!)
        return new TarefaService(repoComCache, validador);
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

    // Método Persistencia removido.

    public static IEventoService criarEventoService() {
        IEventoRepository repositorio = new EventoRepository();
        IValidadorEvento validador = new ValidadorEvento();
        return new EventoService(repositorio, validador);
    }

    public static IEventoController criarEventoController() {
        IEventoService eventoService = criarEventoService();
        return new EventoController(eventoService);
    }

    public static IRelatorioController criarRelatorioController() {
        IRelatorioService relatorioService = criarRelatorioService();
        return new RelatorioController(relatorioService);
    }
}