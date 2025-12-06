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

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(245, 245, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Título ---
        JLabel lblTitulo = new JLabel("Acesso ao Sistema", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(60, 90, 170));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painel.add(lblTitulo, gbc);

        // --- Campos ---
        gbc.gridwidth = 1; 
        
        // E-mail
        gbc.gridy = 1;
        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        painel.add(lblEmail, gbc);
        
        gbc.gridy = 2;
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        // txtEmail.setText("admin@email.com"); // Descomente para agilizar testes
        painel.add(txtEmail, gbc);

        // Senha
        gbc.gridy = 3;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
        painel.add(lblSenha, gbc);
        
        gbc.gridy = 4;
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        painel.add(txtSenha, gbc);

        // --- Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setBackground(new Color(245, 245, 255));
        
        JButton btnLogin = new JButton("Entrar");
        JButton btnCadastrar = new JButton("Criar Conta");
        
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnCadastrar.setFont(new Font("Arial", Font.PLAIN, 14));
        
        painelBotoes.add(btnLogin);
        painelBotoes.add(btnCadastrar);
        
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        painel.add(painelBotoes, gbc);

        add(painel);

        // --- Ações ---
        
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String senha = new String(txtSenha.getPassword());

            if (email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            // Tenta logar (Busca no SQL -> Valida Senha -> Carrega Cache Redis)
            if (sistema.login(email, senha)) {
                // SUCESSO: Passa o sistema JÁ LOGADO para a próxima tela
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

            // Cadastra com Hash
            if (sistema.cadastrarUsuario("Novo Usuário", email, senha)) {
                JOptionPane.showMessageDialog(this, "Conta criada com sucesso! Faça login.");
            } else {
                JOptionPane.showMessageDialog(this, "Erro: E-mail já existe.", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}