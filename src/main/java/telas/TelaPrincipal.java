package telas;
import javax.swing.*;
import negocio.ToDoList;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    private ToDoList sistema;

    // Construtor principal usado pelo Login (RECEBE A SESSÃO)
    public TelaPrincipal(ToDoList sistemaLogado) {
        this.sistema = sistemaLogado;
        
        if (!sistema.isUsuarioLogado()) {
            JOptionPane.showMessageDialog(null, "Erro de sessão. Faça login novamente.");
            dispose();
            new TelaLogin().setVisible(true);
            return;
        }

        inicializarComponentes();
    }

    // Construtor legado (Cria sistema novo - útil apenas para testes rápidos)
    public TelaPrincipal() {
        this.sistema = new ToDoList();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("ToDo List - Sistema de Tarefas");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(criarPainelPrincipal());
    }

    public JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(245, 245, 255));
        painel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Nome do Usuário no Título
        String nomeUsuario = sistema.obterUsuario().getNome();
        JLabel lblTitulo = new JLabel("Olá, " + nomeUsuario + "!", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(60, 90, 170));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        painel.add(lblTitulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(0, 1, 25, 25));
        painelBotoes.setBackground(new Color(245, 245, 255));

        JButton botaoGerenciar = new JButton("Gerenciador de Tarefas");
        JButton botaoRelatorios = new JButton("Relatórios");
        JButton botaoConfig = new JButton("Configurações");
        JButton botaoSair = new JButton("Sair / Logout");

        Font fonteBot = new Font("Arial", Font.PLAIN, 22);
        Dimension tamanhoBotao = new Dimension(0, 50);

        configurarBotao(botaoGerenciar, fonteBot, tamanhoBotao);
        configurarBotao(botaoRelatorios, fonteBot, tamanhoBotao);
        configurarBotao(botaoConfig, fonteBot, tamanhoBotao);
        configurarBotao(botaoSair, fonteBot, tamanhoBotao);

        painelBotoes.add(botaoGerenciar);
        painelBotoes.add(botaoRelatorios);
        painelBotoes.add(botaoConfig);
        painelBotoes.add(botaoSair);

        painel.add(painelBotoes, BorderLayout.CENTER);

        // Ações
        botaoGerenciar.addActionListener(e -> {
            setContentPane(new TelaGerenciadorTarefas(this, sistema));
            revalidate(); repaint();
        });

        botaoRelatorios.addActionListener(e -> {
            setContentPane(new TelaRelatorios(this, sistema));
            revalidate(); repaint();
        });

        botaoConfig.addActionListener(e -> {
            setContentPane(new TelaConfiguracoes(this, sistema));
            revalidate(); repaint();
        });
        
        botaoSair.addActionListener(e -> {
            sistema.logout();
            new TelaLogin().setVisible(true);
            dispose();
        });

        return painel;
    }
    
    private void configurarBotao(JButton btn, Font font, Dimension dim) {
        btn.setFont(font);
        btn.setPreferredSize(dim);
    }
}