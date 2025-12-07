package telas;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Date; // Import necessário para conversão

import modelo.Tarefa;
import negocio.ToDoList;

public class TelaCadastroTarefa extends JPanel {
    private TelaPrincipal janelaPrincipal;
    private ToDoList todoSystem;
    private JTextField campoTitulo;
    private JTextArea areaDescricao;
    private JSpinner spinnerData;
    private JSpinner spinnerPrioridade;

    public TelaCadastroTarefa(TelaPrincipal frame, ToDoList sistema) {
        this.janelaPrincipal = frame;
        this.todoSystem = sistema;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel labelTitulo = new JLabel("Cadastrar Nova Tarefa", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        labelTitulo.setForeground(new Color(60, 90, 170));
        add(labelTitulo, BorderLayout.NORTH);

        // Formulário
        JPanel painelForm = new JPanel(new GridLayout(0, 1, 10, 10));
        painelForm.setBackground(new Color(245, 245, 255));

        painelForm.add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        campoTitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        painelForm.add(campoTitulo);

        painelForm.add(new JLabel("Descrição:"));
        areaDescricao = new JTextArea(10, 20);
        areaDescricao.setFont(new Font("Arial", Font.PLAIN, 18));
        areaDescricao.setLineWrap(true);
        areaDescricao.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDescricao);
        scroll.setPreferredSize(new Dimension(0, 200));
        painelForm.add(scroll);

        painelForm.add(new JLabel("Deadline:"));
        spinnerData = new JSpinner(new SpinnerDateModel());
        spinnerData.setEditor(new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy"));
        spinnerData.setValue(new Date()); // Data atual como padrão
        spinnerData.setFont(new Font("Arial", Font.PLAIN, 18));
        painelForm.add(spinnerData);

        painelForm.add(new JLabel("Prioridade (1-5):"));
        spinnerPrioridade = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        spinnerPrioridade.setFont(new Font("Arial", Font.PLAIN, 18));
        painelForm.add(spinnerPrioridade);

        // Botões
        JButton botSalvar = new JButton("Salvar");
        JButton botCancelar = new JButton("Cancelar");
        botSalvar.setFont(new Font("Arial", Font.PLAIN, 18));
        botCancelar.setFont(new Font("Arial", Font.PLAIN, 18));

        painelForm.add(botSalvar);
        painelForm.add(botCancelar);

        add(painelForm, BorderLayout.CENTER);

        // --- AÇÕES ---
        botSalvar.addActionListener(e -> {
            String titulo = campoTitulo.getText().trim();
            String desc = areaDescricao.getText().trim();

            // Conversão de Data do Spinner para LocalDate
            Date dataEscolhida = (Date) spinnerData.getValue();
            LocalDate deadline = new java.sql.Date(dataEscolhida.getTime()).toLocalDate();

            int prioridade = (Integer) spinnerPrioridade.getValue();

            // CORREÇÃO AQUI: Usar método do Facade (todoSystem) em vez de chamar Service
            // direto
            // O Facade injeta o usuário logado automaticamente.
            if (todoSystem.adicionarTarefa(titulo, desc, deadline, prioridade)) {
                // Sucesso
                // todoSystem.salvarDados(); // (Removido: Persistência é automática via Banco)
                JOptionPane.showMessageDialog(janelaPrincipal, "Tarefa cadastrada com sucesso!");

                // Volta para o gerenciador
                janelaPrincipal.setContentPane(new TelaGerenciadorTarefas(janelaPrincipal, todoSystem));
                janelaPrincipal.revalidate();
                janelaPrincipal.repaint();
            } else {
                // Se falhar (Título vazio ou Usuário não logado)
                JOptionPane.showMessageDialog(janelaPrincipal,
                        "Erro ao cadastrar!\nVerifique se o título foi preenchido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botCancelar.addActionListener(e -> {
            janelaPrincipal.setContentPane(new TelaGerenciadorTarefas(janelaPrincipal, todoSystem));
            janelaPrincipal.revalidate();
            janelaPrincipal.repaint();
        });
    }
}