package controle;

import modelo.Tarefa;
import modelo.Subtarefa;
import modelo.Usuario;
import servicos.RelatorioService;
import servicos.UsuarioService;
import interfaces.IRelatorioService;
import interfaces.IUsuarioService;
import controllers.TarefaController;
import controllers.PersistenciaController;
import comunicacao.Mensageiro;

import java.time.LocalDate;
import java.util.List;

// facade principal do sistema - so coordena controllers
// nao acessa dados diretamente mais
public class ToDoList {
    private ManipuladorDeTarefas gerenciadorTarefas; // ainda precisa pra compatibilidade
    private TarefaService serviceTarefas;
    private SubtarefaService serviceSubs;
    private IRelatorioService relatorioService;
    private IUsuarioService usuarioService;
    private TarefaController tarefaController;
    private PersistenciaController persistenciaController;

    // construtor - inicializa controllers e services
    public ToDoList() {
        // inicializa controllers
        this.persistenciaController = new PersistenciaController();
        this.usuarioService = new UsuarioService();
        
        // carrega dados usando controller
        this.gerenciadorTarefas = persistenciaController.carregarDados();
        
        // se tem usuario salvo, usa ele
        if (gerenciadorTarefas.getUsuario() != null) {
            this.usuarioService = new UsuarioService(gerenciadorTarefas.getUsuario());
        }
        
        // cria services e controllers
        this.serviceTarefas = new TarefaService(gerenciadorTarefas);
        this.serviceSubs = new SubtarefaService(gerenciadorTarefas, serviceTarefas);
        this.relatorioService = new RelatorioService();
        this.tarefaController = new TarefaController(serviceTarefas);
    }

    // salva usando controller de persistencia
    public void salvarDados() {
        persistenciaController.salvarDados(gerenciadorTarefas, usuarioService);
    }

    // ========== GESTÃO DE TAREFAS - USANDO CONTROLLER ==========

    // inclusão de tarefa nova no sistema
    public boolean adicionarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        boolean sucesso = tarefaController.adicionarTarefa(titulo, descricao, deadline, prioridade);
        if (sucesso) salvarDados();
        return sucesso;
    }

    // remoção de tarefa do sistema
    public boolean removerTarefa(String titulo) {
        boolean sucesso = tarefaController.removerTarefa(titulo);
        if (sucesso) salvarDados();
        return sucesso;
    }

    // edição de tarefa existente
    public boolean editarTarefa(String tituloAntigo, String novoTitulo, String novaDescricao, 
                               LocalDate novoDeadline, int novaPrioridade, double novoPercentual) {
        boolean sucesso = tarefaController.editarTarefa(tituloAntigo, novoTitulo, novaDescricao, 
                                                       novoDeadline, novaPrioridade, novoPercentual);
        if (sucesso) salvarDados();
        return sucesso;
    }
    
    // metodos de compatibilidade - ainda precisam pras telas
    public void adicionarTarefa(Tarefa tarefa) {
        gerenciadorTarefas.adicionarTarefa(tarefa);
        salvarDados();
    }
    
    public void removerTarefa(Tarefa tarefa) {
        gerenciadorTarefas.removerTarefa(tarefa);
        salvarDados();
    }
    
    public void editarTarefa(Tarefa antiga, Tarefa nova) {
        gerenciadorTarefas.editarTarefa(antiga, nova);
        salvarDados();
    }

    // listagem completa das tarefas
    public List<Tarefa> listarTodasTarefas() {
        return gerenciadorTarefas.listarTarefas();
    }

    // filtro de tarefas por data específica
    public List<Tarefa> listarTarefasPorData(LocalDate data) {
        return gerenciadorTarefas.listarTarefasPorData(data);
    }

    // busca de tarefas críticas (prazo vencendo)
    public List<Tarefa> listarTarefasCriticas() {
        return gerenciadorTarefas.listarTarefasCriticas();
    }

    // ========== CONTROLE DE SUBTAREFAS ==========

    // adição de subtarefa numa tarefa
    public void adicionarSubtarefa(Tarefa tarefa, Subtarefa subtarefa) {
        gerenciadorTarefas.adicionarSubtarefa(tarefa, subtarefa);
        salvarDados();
    }

    // exclusão de subtarefa
    public void removerSubtarefa(Tarefa tarefa, Subtarefa subtarefa) {
        gerenciadorTarefas.removerSubtarefa(tarefa, subtarefa);
        salvarDados();
    }

    // listagem das subtarefas de uma tarefa
    public List<Subtarefa> listarSubtarefas(Tarefa tarefa) {
        return gerenciadorTarefas.listarSubtarefas(tarefa);
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
        salvarDados(); // persistência da alteração
    }

    // acesso ao manipulador principal
    public ManipuladorDeTarefas getManipuladorTarefas() {
        return gerenciadorTarefas;
    }

    // ========== INTERFACE COM SERVICES ==========

    // disponibilização dos services
    public TarefaService getTarefaService() {
        return serviceTarefas;
    }

    // pega service de subtarefas
    public SubtarefaService obterSubtarefaService() {
        return serviceSubs;
    }

    // localização de tarefa por título - usando controller
    public Tarefa buscarTarefaPorTitulo(String titulo) {
        return tarefaController.buscarTarefa(titulo);
    }
} 