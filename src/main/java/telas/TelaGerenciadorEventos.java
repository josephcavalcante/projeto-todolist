package telas;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import modelo.Evento;
import negocio.ToDoList;

public class TelaGerenciadorEventos extends JPanel {
    private TelaPrincipal janelaPai;
    private ToDoList app;
    private JTable tabelaEventos;
    private DefaultTableModel modeloTabela;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaGerenciadorEventos(TelaPrincipal frame, ToDoList sistema) {
        System.out.println(">>> TelaGerenciadorEventos: Iniciando construtor...");
        this.janelaPai = frame;
        this.app = sistema;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Cabeçalho
        JLabel cabecalho = new JLabel("Gerenciamento de Eventos", JLabel.CENTER);
        cabecalho.setFont(new Font("Arial", Font.BOLD, 28));
        cabecalho.setForeground(new Color(60, 90, 170));
        add(cabecalho, BorderLayout.NORTH);

        // Tabela
        String[] colunas = { "Título", "Data", "Local", "Dias Restantes", "Descrição" };
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaEventos = new JTable(modeloTabela);
        tabelaEventos.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaEventos.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabelaEventos);
        add(scroll, BorderLayout.CENTER);

        // Botões
        JPanel areaBotoes = new JPanel(new GridLayout(1, 4, 10, 10));
        areaBotoes.setBackground(new Color(245, 245, 255));
        areaBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnAdd = new JButton("Adicionar");
        JButton btnEdit = new JButton("Editar");
        JButton btnDel = new JButton("Excluir");
        JButton btnVoltar = new JButton("Voltar");

        areaBotoes.add(btnAdd);
        areaBotoes.add(btnEdit);
        areaBotoes.add(btnDel);
        areaBotoes.add(btnVoltar);

        add(areaBotoes, BorderLayout.SOUTH);

        // Ações
        btnAdd.addActionListener(e -> adicionarEvento());
        btnEdit.addActionListener(e -> editarEvento());
        btnDel.addActionListener(e -> excluirEvento());
        btnVoltar.addActionListener(e -> voltar());

        carregarEventos();
    }

    private void carregarEventos() {
        modeloTabela.setRowCount(0);
        modeloTabela.addRow(new Object[] { "Carregando...", "", "", "", "" });
        System.out.println(">>> TelaGerenciadorEventos: Buscando eventos (Async)...");

        new Thread(() -> {
            List<Evento> eventos = app.listarEventosComDiasRestantes();
            System.out.println(">>> TelaGerenciadorEventos: Encontrados " + eventos.size() + " eventos.");

            SwingUtilities.invokeLater(() -> {
                modeloTabela.setRowCount(0);
                for (Evento ev : eventos) {
                    modeloTabela.addRow(new Object[] {
                            ev.getTitulo(),
                            ev.getDataEvento().format(formatter),
                            ev.getLocal(),
                            ev.diasRestantes() + " dias",
                            ev.getDescricao()
                    });
                }
                tabelaEventos.repaint();
                janelaPai.revalidate();
                janelaPai.repaint();
            });
        }).start();
    }

    private void adicionarEvento() {
        JPanel form = criarFormulario("", "", "", LocalDate.now());
        int result = JOptionPane.showConfirmDialog(janelaPai, form, "Novo Evento", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String tit = ((JTextField) form.getComponent(1)).getText();
                String desc = ((JTextField) form.getComponent(3)).getText();
                String loc = ((JTextField) form.getComponent(5)).getText();
                String dataStr = ((JTextField) form.getComponent(7)).getText();
                LocalDate data = LocalDate.parse(dataStr, formatter);

                if (app.cadastrarEvento(tit, desc, data, loc)) {
                    carregarEventos();
                    JOptionPane.showMessageDialog(janelaPai, "Evento criado!");
                } else {
                    JOptionPane.showMessageDialog(janelaPai,
                            "Erro ao criar (Verifique data duplicada ou campos vazios)");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(janelaPai, "Data inválida! Use dd/MM/yyyy");
            }
        }
    }

    private void editarEvento() {
        int row = tabelaEventos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(janelaPai, "Selecione um evento!");
            return;
        }

        String titAntigo = (String) modeloTabela.getValueAt(row, 0);
        String dataStrAntiga = (String) modeloTabela.getValueAt(row, 1);
        String locAntigo = (String) modeloTabela.getValueAt(row, 2);
        String descAntiga = (String) modeloTabela.getValueAt(row, 4);

        LocalDate dataAntiga = LocalDate.parse(dataStrAntiga, formatter);

        JPanel form = criarFormulario(titAntigo, descAntiga, locAntigo, dataAntiga);
        int result = JOptionPane.showConfirmDialog(janelaPai, form, "Editar Evento", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String novoTit = ((JTextField) form.getComponent(1)).getText();
                String novaDesc = ((JTextField) form.getComponent(3)).getText();
                String novoLoc = ((JTextField) form.getComponent(5)).getText();
                String novaDataStr = ((JTextField) form.getComponent(7)).getText();
                LocalDate novaData = LocalDate.parse(novaDataStr, formatter);

                if (app.editarEvento(titAntigo, dataAntiga, novoTit, novaDesc, novaData, novoLoc)) {
                    carregarEventos();
                    JOptionPane.showMessageDialog(janelaPai, "Evento atualizado!");
                } else {
                    JOptionPane.showMessageDialog(janelaPai, "Erro ao atualizar evento!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(janelaPai, "Erro nos dados!");
            }
        }
    }

    private void excluirEvento() {
        int row = tabelaEventos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(janelaPai, "Selecione um evento!");
            return;
        }

        String tit = (String) modeloTabela.getValueAt(row, 0);
        String dataStr = (String) modeloTabela.getValueAt(row, 1);
        LocalDate data = LocalDate.parse(dataStr, formatter);

        if (JOptionPane.showConfirmDialog(janelaPai, "Excluir " + tit + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (app.removerEvento(tit, data)) {
                carregarEventos();
                JOptionPane.showMessageDialog(janelaPai, "Excluído!");
            } else {
                JOptionPane.showMessageDialog(janelaPai, "Erro ao excluir.");
            }
        }
    }

    private void voltar() {
        janelaPai.setContentPane(janelaPai.criarPainelPrincipal());
        janelaPai.revalidate();
        janelaPai.repaint();
    }

    private JPanel criarFormulario(String t, String d, String l, LocalDate dt) {
        JPanel p = new JPanel(new GridLayout(0, 1));
        p.add(new JLabel("Título:"));
        p.add(new JTextField(t));
        p.add(new JLabel("Descrição:"));
        p.add(new JTextField(d));
        p.add(new JLabel("Local:"));
        p.add(new JTextField(l));
        p.add(new JLabel("Data (dd/MM/yyyy):"));

        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            JFormattedTextField txtData = new JFormattedTextField(mask);
            txtData.setText(dt.format(formatter));
            p.add(txtData);
        } catch (Exception e) {
            p.add(new JTextField(dt.format(formatter)));
        }

        return p;
    }
}
