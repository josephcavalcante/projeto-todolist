package negocio;

import modelo.Tarefa;
import modelo.Subtarefa;
import modelo.Usuario;
import modelo.Evento;
import interfaces.services.*;
import interfaces.controllers.*;
import factories.ServiceFactory;
import comunicacao.Mensageiro;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

public class ToDoList {
    private ITarefaService serviceTarefas;
    private ISubtarefaService serviceSubs;
    private IRelatorioService relatorioService;
    private IUsuarioService usuarioService;
    private IEventoService eventoService;
    private ITarefaController tarefaController;
    private ISubtarefaController subtarefaController;
    private IEventoController eventoController;

    public ToDoList() {
        this.usuarioService = ServiceFactory.criarUsuarioService();
        this.serviceTarefas = ServiceFactory.criarTarefaService();
        this.serviceSubs = ServiceFactory.criarSubtarefaService(serviceTarefas);
        this.relatorioService = ServiceFactory.criarRelatorioService();
        this.eventoService = ServiceFactory.criarEventoService();
        this.tarefaController = ServiceFactory.criarTarefaController();
        this.subtarefaController = ServiceFactory.criarSubtarefaController(serviceTarefas);
        this.eventoController = ServiceFactory.criarEventoController();
    }

    // Login e Usuário
    public boolean login(String email, String senha) {
        return usuarioService.login(email, senha);
    }

    public boolean cadastrarUsuario(String nome, String email, String senha) {
        return usuarioService.cadastrar(nome, email, senha);
    }

    public void logout() {
        usuarioService.logout();
    }

    public boolean isUsuarioLogado() {
        return usuarioService.isLogado();
    }

    public Usuario obterUsuario() {
        return usuarioService.obterUsuario();
    }

    public void setNomeUsuario(String novoNome) {
        usuarioService.alterarNome(novoNome);
    }

    public IUsuarioService getUsuarioService() {
        return usuarioService;
    }

    // --- MÉTODOS DE TAREFAS (CORRIGIDOS COM FILTRO DE USUÁRIO) ---

    public boolean adicionarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        Usuario usuario = usuarioService.obterUsuario();
        if (usuario == null)
            return false;
        // Chama o controller/service passando o usuário dono da tarefa
        return tarefaController.adicionarTarefa(titulo, descricao, deadline, prioridade, usuario);
    }

    public boolean removerTarefa(String titulo) {
        // Idealmente, verificar se a tarefa pertence ao usuário antes de remover
        return tarefaController.removerTarefa(titulo);
    }

    public boolean editarTarefa(String tituloAntigo, String novoTitulo, String novaDescricao,
            LocalDate novoDeadline, int novaPrioridade, double novoPercentual) {
        return tarefaController.editarTarefa(tituloAntigo, novoTitulo, novaDescricao,
                novoDeadline, novaPrioridade, novoPercentual);
    }

    // LISTAR: Agora filtra pelo usuário logado (via Controller)
    public List<Tarefa> listarTodasTarefas() {
        Usuario usuario = usuarioService.obterUsuario();
        if (usuario != null) {
            return tarefaController.listarTodas(usuario);
        }
        return Collections.emptyList();
    }

    public List<Tarefa> listarTarefasPorData(LocalDate data) {
        Usuario usuario = usuarioService.obterUsuario();
        if (usuario != null) {
            return tarefaController.listarPorData(data, usuario);
        }
        return Collections.emptyList();
    }

    public List<Tarefa> listarTarefasCriticas() {
        Usuario usuario = usuarioService.obterUsuario();
        if (usuario != null) {
            return tarefaController.listarCriticas(usuario);
        }
        return Collections.emptyList();
    }

    public Tarefa buscarTarefaPorTitulo(String titulo) {
        return tarefaController.buscarTarefa(titulo);
    }

    public ITarefaService getTarefaService() {
        return serviceTarefas;
    }

    // --- SUBTAREFAS ---
    public boolean adicionarSubtarefa(String tituloTarefa, String titulo, String descricao, double percentual) {
        return subtarefaController.adicionarSubtarefa(tituloTarefa, titulo, descricao, percentual);
    }

    public boolean removerSubtarefa(String tituloTarefa, String titulo) {
        return subtarefaController.removerSubtarefa(tituloTarefa, titulo);
    }

    public List<Subtarefa> listarSubtarefas(String tituloTarefa) {
        return subtarefaController.listarSubtarefas(tituloTarefa);
    }

    public List<Subtarefa> listarSubtarefas(Tarefa tarefa) {
        return listarSubtarefas(tarefa.getTitulo());
    }

    public ISubtarefaService obterSubtarefaService() {
        return serviceSubs;
    }

    // --- RELATÓRIOS ---
    public boolean gerarRelatorioPDF(LocalDate data) {
        return relatorioService.gerarPDF(listarTarefasPorData(data), data);
    }

    public boolean enviarRelatorioEmail(LocalDate data) {
        try {
            relatorioService.gerarPDF(listarTarefasPorData(data), data);
            Mensageiro.enviarEmail(usuarioService.obterEmail(), "Relatório do dia " + data);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean gerarRelatorioExcel(int mes, int ano) {
        // Gera Excel apenas das tarefas do usuário logado
        return relatorioService.gerarExcel(listarTodasTarefas(), mes, ano);
    }

    // --- EVENTOS ---
    public boolean cadastrarEvento(String titulo, String descricao, LocalDate dataEvento, String local) {
        return eventoController.cadastrarEvento(titulo, descricao, dataEvento, local);
    }

    public boolean editarEvento(String tituloAntigo, LocalDate dataAntiga, String novoTitulo,
            String novaDescricao, LocalDate novaData, String novoLocal) {
        return eventoController.editarEvento(tituloAntigo, dataAntiga, novoTitulo, novaDescricao, novaData, novoLocal);
    }

    public boolean removerEvento(String titulo, LocalDate dataEvento) {
        return eventoController.removerEvento(titulo, dataEvento);
    }

    public List<Evento> listarEventosComDiasRestantes() {
        return eventoController.listarEventosComDiasRestantes();
    }

    public List<Evento> listarEventosPorData(LocalDate data) {
        return eventoController.listarEventosPorData(data);
    }

    public List<Evento> listarEventosPorMes(int mes, int ano) {
        return eventoController.listarEventosPorMes(mes, ano);
    }

    public Evento buscarEvento(String titulo, LocalDate dataEvento) {
        return eventoController.buscarEvento(titulo, dataEvento);
    }
}