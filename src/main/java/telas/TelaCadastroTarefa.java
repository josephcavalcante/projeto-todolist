package telas;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

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
        this.todoSystem = sistema; // referencia pro sistema
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // titulo da tela
        JLabel labelTitulo = new JLabel("Cadastrar Nova Tarefa", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        labelTitulo.setForeground(new Color(60, 90, 170));
        add(labelTitulo, BorderLayout.NORTH);

        // painel com os campos do formulario
        JPanel painelForm = new JPanel(new GridLayout(0, 1, 10, 10));
        painelForm.setBackground(new Color(245, 245, 255));

        painelForm.add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        campoTitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        painelForm.add(campoTitulo);

        painelForm.add(new JLabel("Descrição:"));
        areaDescricao = new JTextArea(10, 20);
        areaDescricao.setFont(new Font("Arial", Font.PLAIN, 18));
        areaDescricao.setLineWrap(true); // quebra linha automatica
        areaDescricao.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDescricao);
        scroll.setPreferredSize(new Dimension(0, 200));
        painelForm.add(scroll);

        painelForm.add(new JLabel("Deadline:"));
        spinnerData = new JSpinner(new SpinnerDateModel());
        spinnerData.setEditor(new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy"));
        spinnerData.setValue(java.sql.Date.valueOf(LocalDate.now().plusDays(7))); // padrao 7 dias
        spinnerData.setFont(new Font("Arial", Font.PLAIN, 18));
        painelForm.add(spinnerData);

        painelForm.add(new JLabel("Prioridade (1-5):"));
        spinnerPrioridade = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        spinnerPrioridade.setFont(new Font("Arial", Font.PLAIN, 18));
        painelForm.add(spinnerPrioridade);

        // botoes de acao
        JButton botSalvar = new JButton("Salvar");
        JButton botCancelar = new JButton("Cancelar");
        botSalvar.setFont(new Font("Arial", Font.PLAIN, 18));
        botCancelar.setFont(new Font("Arial", Font.PLAIN, 18));

        painelForm.add(botSalvar);
        painelForm.add(botCancelar);

        add(painelForm, BorderLayout.CENTER);

        // eventos dos botoes
        botSalvar.addActionListener(e -> {
            // pega os dados dos campos
            String titulo = campoTitulo.getText().trim();
            String desc = areaDescricao.getText().trim();
            java.util.Date dataEscolhida = (java.util.Date) spinnerData.getValue();
            LocalDate deadline = dataEscolhida.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            int prioridade = (Integer) spinnerPrioridade.getValue();
            
            // tenta cadastrar a tarefa
            if (todoSystem.getTarefaService().cadastrar(titulo, desc, deadline, prioridade)) {
                todoSystem.salvarDados();
                JOptionPane.showMessageDialog(janelaPrincipal, "Tarefa cadastrada com sucesso!");
                // volta pro menu
                janelaPrincipal.setContentPane(new TelaGerenciadorTarefas(janelaPrincipal, todoSystem));
                janelaPrincipal.revalidate();
                janelaPrincipal.repaint();
            } else {
                JOptionPane.showMessageDialog(janelaPrincipal, "Erro: Título é obrigatório!");
            }
        });

        botCancelar.addActionListener(e -> {
            // volta pro gerenciador sem salvar
            janelaPrincipal.setContentPane(new TelaGerenciadorTarefas(janelaPrincipal, todoSystem));
            janelaPrincipal.revalidate();
            janelaPrincipal.repaint();
        });
    }
}