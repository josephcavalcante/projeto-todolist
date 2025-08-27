package telas;
import javax.swing.*;
import java.awt.*;
import controle.ToDoList;

public class TelaConfiguracoes extends JPanel {
    private TelaPrincipal janela;
    private ToDoList sistemaApp;
    private JTextField campoNome;
    private JLabel labelEmail;

    public TelaConfiguracoes(TelaPrincipal frame, ToDoList sistema) {
        this.janela = frame;
        this.sistemaApp = sistema; // sistema principal
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // titulo da pagina
        JLabel tituloTela = new JLabel("Configurações do Usuário", JLabel.CENTER);
        tituloTela.setFont(new Font("Arial", Font.BOLD, 28));
        tituloTela.setForeground(new Color(60, 90, 170));
        add(tituloTela, BorderLayout.NORTH);

        // painel dos campos
        JPanel painel = new JPanel(new GridLayout(0, 1, 10, 10));
        painel.setBackground(new Color(245, 245, 255));

        campoNome = new JTextField(sistemaApp.obterUsuario().getNome());
        labelEmail = new JLabel("E-mail: " + sistemaApp.obterUsuario().getEmail()); // pega email
        campoNome.setFont(new Font("Arial", Font.PLAIN, 18));
        labelEmail.setFont(new Font("Arial", Font.PLAIN, 18));

        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(labelEmail);

        // botoes
        JButton botaoSalvar = new JButton("Salvar Alterações");
        JButton botaoVoltar = new JButton("Voltar");
        botaoSalvar.setFont(new Font("Arial", Font.PLAIN, 18));
        botaoVoltar.setFont(new Font("Arial", Font.PLAIN, 18));

        painel.add(botaoSalvar);
        painel.add(botaoVoltar);

        add(painel, BorderLayout.CENTER);

        // acoes dos botoes
        botaoSalvar.addActionListener(e -> {
            String nomeNovo = campoNome.getText().trim();
            if(nomeNovo.isEmpty()) { // sem espaço no if
                JOptionPane.showMessageDialog(janela, "Nome não pode estar vazio!");
                return;
            }
            // salva o nome novo
            sistemaApp.setNomeUsuario(nomeNovo);
            JOptionPane.showMessageDialog(janela, "Nome alterado com sucesso!");
        });

        botaoVoltar.addActionListener(e -> {
            // volta pro menu principal
            janela.setContentPane(janela.criarPainelPrincipal());
            janela.revalidate();
            janela.repaint();
        });
    }
}
