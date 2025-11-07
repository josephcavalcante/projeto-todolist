package telas;
import javax.swing.*;

import negocio.ToDoList;

import java.awt.*;

public class TelaPrincipal extends JFrame {
    private ToDoList sistema;

    public TelaPrincipal() {
        // inicializa o sistema
        sistema = new ToDoList();
        setTitle("ToDo List - Sistema de Tarefas");
        setSize(800,600); // tamanho bom pra tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza na tela
        setResizable(false); // nao permite redimensionar

        setContentPane(criarPainelPrincipal());
    }

    public JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(245, 245, 255)); // azul bem clarinho
        painel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // margem de 40px

        // titulo da aplicacao
        JLabel lblTitulo = new JLabel("ToDo List - Sistema de Tarefas", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 36)); // fonte grande pro titulo
        lblTitulo.setForeground(new Color(60, 90, 170)); // azul escuro
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        painel.add(lblTitulo, BorderLayout.NORTH);

        // painel dos botoes principais
        JPanel painelBotoes = new JPanel(new GridLayout(0, 1, 25, 25));
        painelBotoes.setBackground(new Color(245, 245, 255));

        // criando os botoes
        JButton botaoGerenciar = new JButton("Gerenciador de Tarefas");
        JButton botaoRelatorios = new JButton("Relatórios");
        JButton botaoConfig = new JButton("Configurações");

        // ajusta fonte dos botoes
        Font fonteBot = new Font("Arial", Font.PLAIN, 22); // tamanho 22
        botaoGerenciar.setFont(fonteBot);
        botaoRelatorios.setFont(fonteBot);
        botaoConfig.setFont(fonteBot);
        
        // altura dos botoes
        Dimension tamanhoBotao = new Dimension(0, 60); // 60 pixels de altura
        botaoGerenciar.setPreferredSize(tamanhoBotao);
        botaoRelatorios.setPreferredSize(tamanhoBotao);
        botaoConfig.setPreferredSize(tamanhoBotao);

        painelBotoes.add(botaoGerenciar);
        painelBotoes.add(botaoRelatorios);
        painelBotoes.add(botaoConfig);

        painel.add(painelBotoes, BorderLayout.CENTER);

        // acoes dos botoes
        botaoGerenciar.addActionListener(e -> {
            setContentPane(new TelaGerenciadorTarefas(this, sistema));
            revalidate();
            repaint();
        });

        botaoRelatorios.addActionListener(e -> {
            setContentPane(new TelaRelatorios(this, sistema));
            revalidate();
            repaint();
        });

        botaoConfig.addActionListener(e -> {
            setContentPane(new TelaConfiguracoes(this, sistema));
            revalidate();
            repaint();
        });

        return painel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}