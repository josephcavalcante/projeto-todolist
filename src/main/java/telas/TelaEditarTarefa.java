package telas;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import modelo.Tarefa;
import negocio.ToDoList;

public class TelaEditarTarefa extends JPanel {
    private final TelaPrincipal frame;
    private final ToDoList sistema;
    private final Tarefa tarefaOriginal;
    private final JTextField txtTitulo;
    private final JTextArea txtDescricao;
    private final JSpinner spnDeadline;
    private final JSpinner spnPrioridade;
    private final JSpinner spnPercentual;

    public TelaEditarTarefa(TelaPrincipal frame, ToDoList sistema, Tarefa tarefa) {
        this.frame = frame;
        this.sistema = sistema;
        this.tarefaOriginal = tarefa;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tituloLabel = new JLabel("Editar Tarefa", JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 28));
        tituloLabel.setForeground(new Color(60, 90, 170));
        add(tituloLabel, BorderLayout.NORTH);

        JPanel painelCampos = new JPanel(new GridLayout(0, 1, 10, 10));
        painelCampos.setBackground(new Color(245, 245, 255));

        painelCampos.add(new JLabel("Título:"));
        txtTitulo = new JTextField(tarefa.getTitulo());
        txtTitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        painelCampos.add(txtTitulo);

        painelCampos.add(new JLabel("Descrição:"));
        txtDescricao = new JTextArea(10, 20);
        txtDescricao.setText(tarefa.getDescricao());
        txtDescricao.setFont(new Font("Arial", Font.PLAIN, 18));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setPreferredSize(new Dimension(0, 200));
        painelCampos.add(scrollDescricao);

        painelCampos.add(new JLabel("Deadline:"));
        spnDeadline = new JSpinner(new SpinnerDateModel());
        spnDeadline.setEditor(new JSpinner.DateEditor(spnDeadline, "dd/MM/yyyy"));
        spnDeadline.setValue(java.sql.Date.valueOf(tarefa.getDeadline()));
        spnDeadline.setFont(new Font("Arial", Font.PLAIN, 18));
        painelCampos.add(spnDeadline);

        painelCampos.add(new JLabel("Prioridade (1-5):"));
        spnPrioridade = new JSpinner(new SpinnerNumberModel(tarefa.getPrioridade(), 1, 5, 1));
        spnPrioridade.setFont(new Font("Arial", Font.PLAIN, 18));
        painelCampos.add(spnPrioridade);

        painelCampos.add(new JLabel("Percentual (0-100):"));
        spnPercentual = new JSpinner(new SpinnerNumberModel(tarefa.getPercentual(), 0.0, 100.0, 1.0));
        spnPercentual.setFont(new Font("Arial", Font.PLAIN, 18));
        painelCampos.add(spnPercentual);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        btnSalvar.setFont(new Font("Arial", Font.PLAIN, 18));
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 18));

        painelCampos.add(btnSalvar);
        painelCampos.add(btnCancelar);

        add(painelCampos, BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> salvarEdicao());

        btnCancelar.addActionListener(e -> voltarParaLista());
    }

    private void salvarEdicao() {
        String novoTitulo = txtTitulo.getText().trim();
        String novaDescricao = txtDescricao.getText().trim();
        java.util.Date dateValue = (java.util.Date) spnDeadline.getValue();
        LocalDate novoDeadline = dateValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        int novaPrioridade = (Integer) spnPrioridade.getValue();
        double novoPercentual = (Double) spnPercentual.getValue();

        if (sistema.editarTarefa(tarefaOriginal.getTitulo(), novoTitulo, novaDescricao, novoDeadline,
                novaPrioridade, novoPercentual)) {
            JOptionPane.showMessageDialog(frame, "Tarefa editada com sucesso!");
            voltarParaLista();
        } else {
            JOptionPane.showMessageDialog(frame, "Erro: Título é obrigatório!");
        }
    }

    private void voltarParaLista() {
        frame.setContentPane(new TelaListarTarefas(frame, sistema, "Todas as Tarefas"));
        frame.revalidate();
        frame.repaint();
    }
}