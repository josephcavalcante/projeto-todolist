package telas;

import javax.swing.*;
import java.awt.*;
import negocio.ToDoList;

public class TelaLogin extends JFrame {
    private ToDoList sistema;
    private JTextField txtEmail;
    private JPasswordField txtSenha;

    public TelaLogin() {
        // Inicializa o Facade (que carrega Services, Repositories e Redis)
        this.sistema = new ToDoList();

        setTitle("Login - ToDo List");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        // --- Título ---
        JLabel lblTitulo = new JLabel("Acesso ao Sistema", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(60, 90, 170));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // --- Campos ---
        JPanel painelCampos = new JPanel(new GridLayout(4, 1, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        painelCampos.setBackground(new Color(245, 245, 255));

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));

        painelCampos.add(lblEmail);
        painelCampos.add(txtEmail);
        painelCampos.add(lblSenha);
        painelCampos.add(txtSenha);

        add(painelCampos, BorderLayout.CENTER);

        // --- Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painelBotoes.setBackground(new Color(245, 245, 255));

        JButton btnLogin = new JButton("Entrar");
        JButton btnCadastrar = new JButton("Criar Conta");

        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnCadastrar.setFont(new Font("Arial", Font.PLAIN, 14));

        painelBotoes.add(btnLogin);
        painelBotoes.add(btnCadastrar);

        add(painelBotoes, BorderLayout.SOUTH);

        // --- Ações ---
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String senha = new String(txtSenha.getPassword());

            if (email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            if (sistema.login(email, senha)) {
                new TelaPrincipal(sistema).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCadastrar.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String senha = new String(txtSenha.getPassword());

            if (email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Para cadastrar, preencha E-mail e Senha.");
                return;
            }

            if (sistema.cadastrarUsuario("Novo Usuário", email, senha)) {
                JOptionPane.showMessageDialog(this, "Conta criada com sucesso! Faça login.");
            } else {
                JOptionPane.showMessageDialog(this, "Erro: E-mail já existe.", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}