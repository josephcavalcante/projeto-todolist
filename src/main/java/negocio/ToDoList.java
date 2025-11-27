package negocio;

import modelo.Tarefa;
import modelo.Subtarefa;
import modelo.Usuario;
import interfaces.services.IRelatorioService;
import interfaces.services.IUsuarioService;
import interfaces.services.ISubtarefaService;
import interfaces.services.IEventoService;
import interfaces.services.ITarefaService;
import interfaces.controllers.ITarefaController;
import interfaces.controllers.ISubtarefaController;
import interfaces.controllers.IEventoController;
import factories.ServiceFactory;
import comunicacao.Mensageiro;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe principal que atua como Facade do sistema ToDoList.
 * <p>
 * Coordena operações através de controllers especializados, mantendo
 * baixo acoplamento e alta coesão. Aplica os princípios SOLID e GRASP,
 * servindo como ponto de entrada único para o sistema.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 1.0
 */
public class ToDoList {
    private ITarefaService serviceTarefas;
    private ISubtarefaService serviceSubs;
    private IRelatorioService relatorioService;
    private IUsuarioService usuarioService;
    private IEventoService eventoService;
    private ITarefaController tarefaController;
    private ISubtarefaController subtarefaController;
    private IEventoController eventoController;

    /**
     * Construtor que inicializa o sistema usando ServiceFactory.
     * <p>
     * Aplica injeção de dependência através da factory, seguindo
     * o princípio DIP (Dependency Inversion Principle).
     * </p>
     */
    public ToDoList() {
        // inicializa usando factory
        this.usuarioService = ServiceFactory.criarUsuarioService();

        // cria services e controllers usando factory
        this.serviceTarefas = ServiceFactory.criarTarefaService();
        this.serviceSubs = ServiceFactory.criarSubtarefaService(serviceTarefas);
        this.relatorioService = ServiceFactory.criarRelatorioService();
        this.eventoService = ServiceFactory.criarEventoService();
        this.tarefaController = ServiceFactory.criarTarefaController();
        this.subtarefaController = ServiceFactory.criarSubtarefaController(serviceTarefas);
        this.eventoController = ServiceFactory.criarEventoController();
    }

    // Mantido para compatibilidade com a UI antiga (que chama este método)
    // A persistência agora é automática via JPA/Mongo
    public void salvarDados() {
    }

    // ========== GESTÃO DE TAREFAS - USANDO CONTROLLER ==========

    // inclusão de tarefa nova no sistema
    public boolean adicionarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        return tarefaController.adicionarTarefa(titulo, descricao, deadline, prioridade);
    }

    // remoção de tarefa do sistema
    public boolean removerTarefa(String titulo) {
        return tarefaController.removerTarefa(titulo);
    }

    // edição de tarefa existente
    public boolean editarTarefa(String tituloAntigo, String novoTitulo, String novaDescricao,
            LocalDate novoDeadline, int novaPrioridade, double novoPercentual) {
        return tarefaController.editarTarefa(tituloAntigo, novoTitulo, novaDescricao,
                novoDeadline, novaPrioridade, novoPercentual);
    }

    // metodos de compatibilidade - ainda precisam pras telas
    public void adicionarTarefa(Tarefa tarefa) {
        // Adaptando para usar o controller/service
        tarefaController.adicionarTarefa(tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getDeadline(),
                tarefa.getPrioridade());
    }

    public void removerTarefa(Tarefa tarefa) {
        tarefaController.removerTarefa(tarefa.getTitulo());
    }

    public void editarTarefa(Tarefa antiga, Tarefa nova) {
        tarefaController.editarTarefa(antiga.getTitulo(), nova.getTitulo(), nova.getDescricao(),
                nova.getDeadline(), nova.getPrioridade(), nova.getPercentual());
    }

    // listagem completa das tarefas
    public List<Tarefa> listarTodasTarefas() {
        return serviceTarefas.listarTodas();
    }

    // filtro de tarefas por data específica
    public List<Tarefa> listarTarefasPorData(LocalDate data) {
        return serviceTarefas.listarPorData(data);
    }

    // busca de tarefas críticas (prazo vencendo)
    public List<Tarefa> listarTarefasCriticas() {
        return serviceTarefas.listarCriticas();
    }

    // ========== CONTROLE DE SUBTAREFAS - USANDO CONTROLLER ==========

    // adição de subtarefa usando controller
    public boolean adicionarSubtarefa(String tituloTarefa, String titulo, String descricao, double percentual) {
        return subtarefaController.adicionarSubtarefa(tituloTarefa, titulo, descricao, percentual);
    }

    // exclusão de subtarefa usando controller
    public boolean removerSubtarefa(String tituloTarefa, String titulo) {
        return subtarefaController.removerSubtarefa(tituloTarefa, titulo);
    }

    // listagem das subtarefas usando controller
    public List<Subtarefa> listarSubtarefas(String tituloTarefa) {
        return subtarefaController.listarSubtarefas(tituloTarefa);
    }

    // metodos de compatibilidade - ainda precisam pras telas
    public void adicionarSubtarefa(Tarefa tarefa, Subtarefa subtarefa) {
        subtarefaController.adicionarSubtarefa(tarefa.getTitulo(), subtarefa.getTitulo(),
                subtarefa.getDescricao(), subtarefa.getPercentual());
    }

    public void removerSubtarefa(Tarefa tarefa, Subtarefa subtarefa) {
        subtarefaController.removerSubtarefa(tarefa.getTitulo(), subtarefa.getTitulo());
    }

    public List<Subtarefa> listarSubtarefas(Tarefa tarefa) {
        return subtarefaController.listarSubtarefas(tarefa.getTitulo());
    }

    // ========== GERAÇÃO DE RELATÓRIOS ==========

    // criação de PDF com tarefas do dia
    public boolean gerarRelatorioPDF(LocalDate data) {
        List<Tarefa> tarefasDoDia = listarTarefasPorData(data);
        return relatorioService.gerarPDF(tarefasDoDia, data);
    }

    // envio de email com relatório diário
    public boolean enviarRelatorioEmail(LocalDate data) {
        try {
            List<Tarefa> tarefas = listarTarefasPorData(data);
            relatorioService.gerarPDF(tarefas, data);
            Mensageiro.enviarEmail(usuarioService.obterEmail(), "Relatório de tarefas do dia " + data);
            return true;
        } catch (Exception ex) {
            System.out.println("Erro ao enviar e-mail: " + ex.getMessage());
            return false;
        }
    }

    // geração de planilha Excel mensal
    public boolean gerarRelatorioExcel(int mes, int ano) {
        List<Tarefa> todasTarefas = listarTodasTarefas();
        return relatorioService.gerarExcel(todasTarefas, mes, ano);
    }

    // ========== DADOS DO USUÁRIO ==========

    // pega usuário logado
    public Usuario obterUsuario() {
        return usuarioService.obterUsuario();
    }

    // alteração do nome do usuário (email permanece fixo)
    public void setNomeUsuario(String novoNome) {
        usuarioService.alterarNome(novoNome);
    }

    // ========== INTERFACE COM SERVICES ==========

    // disponibilização dos services
    public ITarefaService getTarefaService() {
        return serviceTarefas;
    }

    // pega service de subtarefas
    public ISubtarefaService obterSubtarefaService() {
        return serviceSubs;
    }

    // localização de tarefa por título - usando controller
    public Tarefa buscarTarefaPorTitulo(String titulo) {
        return tarefaController.buscarTarefa(titulo);
    }

    // ========== GESTÃO DE EVENTOS - USANDO CONTROLLER ==========

    /**
     * Cadastra um novo evento no sistema.
     * 
     * @param titulo     título do evento
     * @param descricao  descrição do evento
     * @param dataEvento data do evento
     * @param local      local do evento
     * @return true se cadastrado com sucesso, false caso contrário
     */
    public boolean cadastrarEvento(String titulo, String descricao, LocalDate dataEvento, String local) {
        return eventoController.cadastrarEvento(titulo, descricao, dataEvento, local);
    }

    /**
     * Edita um evento existente.
     * 
     * @param tituloAntigo  título atual do evento
     * @param dataAntiga    data atual do evento
     * @param novoTitulo    novo título
     * @param novaDescricao nova descrição
     * @param novaData      nova data
     * @param novoLocal     novo local
     * @return true se editado com sucesso, false caso contrário
     */
    public boolean editarEvento(String tituloAntigo, LocalDate dataAntiga, String novoTitulo,
            String novaDescricao, LocalDate novaData, String novoLocal) {
        return eventoController.editarEvento(tituloAntigo, dataAntiga, novoTitulo, novaDescricao, novaData, novoLocal);
    }

    /**
     * Remove um evento do sistema.
     * 
     * @param titulo     título do evento
     * @param dataEvento data do evento
     * @return true se removido com sucesso, false caso contrário
     */
    public boolean removerEvento(String titulo, LocalDate dataEvento) {
        return eventoController.removerEvento(titulo, dataEvento);
    }

    /**
     * Lista todos os eventos com informações de dias restantes.
     * 
     * @return lista de eventos
     */
    public List<modelo.Evento> listarEventosComDiasRestantes() {
        return eventoController.listarEventosComDiasRestantes();
    }

    /**
     * Lista eventos de uma data específica.
     * 
     * @param data data para filtrar
     * @return lista de eventos da data
     */
    public List<modelo.Evento> listarEventosPorData(LocalDate data) {
        return eventoController.listarEventosPorData(data);
    }

    /**
     * Lista eventos de um mês específico.
     * 
     * @param mes mês (1-12)
     * @param ano ano
     * @return lista de eventos do mês
     */
    public List<modelo.Evento> listarEventosPorMes(int mes, int ano) {
        return eventoController.listarEventosPorMes(mes, ano);
    }

    /**
     * Busca um evento específico.
     * 
     * @param titulo     título do evento
     * @param dataEvento data do evento
     * @return o evento encontrado ou null
     */
    public modelo.Evento buscarEvento(String titulo, LocalDate dataEvento) {
        return eventoController.buscarEvento(titulo, dataEvento);
    }
}