package controle;

import modelo.Tarefa;
import modelo.Subtarefa;
import modelo.Usuario;
import persistencia.Persistencia;
import relatorios.GeradorDeRelatorios;
import comunicacao.Mensageiro;

import java.time.LocalDate;
import java.util.List;

// classe principal do sistema
// faz a orquestracao de tudo
public class ToDoList {
    private ManipuladorDeTarefas gerenciadorTarefas;
    private Persistencia salvaDados;
    private Usuario usuarioAtual;
    private TarefaService serviceTarefas;
    private SubtarefaService serviceSubs;
    private static final String ARQUIVO_DADOS = "todolist.dat"; // arquivo onde salva

    // construtor - inicializa tudo
    public ToDoList() {
        this.gerenciadorTarefas = new ManipuladorDeTarefas();
        this.salvaDados = new Persistencia();
        this.usuarioAtual = new Usuario("Usuário", "projetopoo00@gmail.com"); // email fixo
        
        // tenta carregar dados salvos
        carregarDados();
        
        // cria os services
        this.serviceTarefas = new TarefaService(gerenciadorTarefas);
        this.serviceSubs = new SubtarefaService(gerenciadorTarefas, serviceTarefas);
    }

    // carrega os dados do arquivo
    private void carregarDados() {
        try {
            ManipuladorDeTarefas dadosCarregados = salvaDados.carregarManipulador(ARQUIVO_DADOS);
            if (dadosCarregados != null) {
                this.gerenciadorTarefas = dadosCarregados; // substitui o vazio
                // se tem usuario salvo, usa ele
                if (dadosCarregados.getUsuario() != null) {
                    this.usuarioAtual = dadosCarregados.getUsuario();
                }
            }
        } catch (Exception erro) {
            System.out.println("Arquivo de dados não encontrado. Iniciando com dados vazios.");
        }
    }

    // salva tudo no arquivo
    public void salvarDados() {
        try {
            gerenciadorTarefas.setUsuario(usuarioAtual); // inclui o usuario
            salvaDados.salvarManipulador(gerenciadorTarefas, ARQUIVO_DADOS);
        } catch (Exception ex) {
            System.out.println("Erro ao salvar dados: " + ex.getMessage());
        }
    }

    // ========== GESTÃO DE TAREFAS ==========

    // inclusão de tarefa nova no sistema
    public void adicionarTarefa(Tarefa tarefa) {
        gerenciadorTarefas.adicionarTarefa(tarefa);
        salvarDados(); // gravação automática
    }

    // remoção de tarefa do sistema
    public void removerTarefa(Tarefa tarefa) {
        gerenciadorTarefas.removerTarefa(tarefa);
        salvarDados();
    }

    // edição de tarefa existente
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
        try {
            List<Tarefa> tarefasDoDia = listarTarefasPorData(data);
            GeradorDeRelatorios.gerarRelatorioPDF(tarefasDoDia, data);
            return true; // sucesso na operação
        } catch (Exception erro) {
            System.out.println("Erro ao gerar relatório PDF: " + erro.getMessage());
            return false; // falha na operação
        }
    }

    // envio de email com relatório diário
    public boolean enviarRelatorioEmail(LocalDate data) {
        try {
            List<Tarefa> tarefas = listarTarefasPorData(data);
            GeradorDeRelatorios.gerarRelatorioPDF(tarefas, data);
            Mensageiro.enviarEmail(usuarioAtual.getEmail(), "Relatório de tarefas do dia " + data);
            return true;
        } catch (Exception ex) {
            System.out.println("Erro ao enviar e-mail: " + ex.getMessage());
            return false;
        }
    }

    // geração de planilha Excel mensal
    public boolean gerarRelatorioExcel(int mes, int ano) {
        try {
            List<Tarefa> todasTarefas = listarTodasTarefas();
            GeradorDeRelatorios.gerarRelatorioExcel(todasTarefas, mes, ano);
            return true;
        } catch (Exception erro) {
            System.out.println("Erro ao gerar relatório Excel: " + erro.getMessage());
            return false;
        }
    }

    // ========== DADOS DO USUÁRIO ==========

    // pega usuário logado
    public Usuario obterUsuario() {
        return usuarioAtual;
    }

    // alteração do nome do usuário (email permanece fixo)
    public void setNomeUsuario(String novoNome) {
        this.usuarioAtual.setNome(novoNome);
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

    // localização de tarefa por título
    public Tarefa buscarTarefaPorTitulo(String titulo) {
        return serviceTarefas.buscarPorTitulo(titulo);
    }
} 