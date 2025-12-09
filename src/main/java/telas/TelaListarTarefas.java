package telas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import modelo.Tarefa;
import negocio.ToDoList;

import java.util.List;

import interfaces.observer.IObserver;

public class TelaListarTarefas extends JPanel implements IObserver {
    private final TelaPrincipal frame;
    private final ToDoList sistema;
    private final String tituloTela;
    private final JTable tabela;
    private final DefaultTableModel modelo;

    public TelaListarTarefas(TelaPrincipal frame, ToDoList sistema, String tituloTela) {
        this(frame, sistema, tituloTela, null);
    }

    public TelaListarTarefas(TelaPrincipal frame, ToDoList sistema, String tituloTela, String dataStr) {
        this.frame = frame;
        this.sistema = sistema;
        this.tituloTela = tituloTela;
        this.dataFiltro = dataStr; // Inicializa filtro para refresh

        // Registra para receber atualizações do Backend
        this.sistema.getTarefaService().adicionarObservador(this);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel(tituloTela, JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(60, 90, 170));
        add(titulo, BorderLayout.NORTH);

        // Tabela
        String[] colunas = { "Título", "Descrição", "Deadline", "Prioridade", "Percentual" };
        modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setPreferredSize(new Dimension(0, 300));
        add(scrollTabela, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new GridLayout(1, 4, 10, 10));
        painelBotoes.setBackground(new Color(245, 245, 255));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnSubtarefas = new JButton("Subtarefas");
        JButton btnVoltar = new JButton("Voltar");

        btnEditar.setFont(new Font("Arial", Font.PLAIN, 16));
        btnExcluir.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSubtarefas.setFont(new Font("Arial", Font.PLAIN, 16));
        btnVoltar.setFont(new Font("Arial", Font.PLAIN, 16));

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnSubtarefas);
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                String tituloTarefa = (String) modelo.getValueAt(linha, 0);
                Tarefa tarefa = sistema.buscarTarefaPorTitulo(tituloTarefa);
                if (tarefa != null) {
                    frame.setContentPane(new TelaEditarTarefa(frame, sistema, tarefa));
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Tarefa não encontrada!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione uma tarefa!");
            }
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                String tituloTarefa = (String) modelo.getValueAt(linha, 0);
                int confirmacao = JOptionPane.showConfirmDialog(frame,
                        "Excluir tarefa: " + tituloTarefa + "?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    if (sistema.getTarefaService().excluir(tituloTarefa)) {
                        modelo.removeRow(linha);
                        JOptionPane.showMessageDialog(frame, "Tarefa excluída!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Erro ao excluir tarefa!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione uma tarefa!");
            }
        });

        btnSubtarefas.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                String tituloTarefa = (String) modelo.getValueAt(linha, 0);
                Tarefa tarefa = sistema.buscarTarefaPorTitulo(tituloTarefa);
                if (tarefa != null) {
                    frame.setContentPane(new TelaSubtarefas(frame, sistema, tarefa));
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Tarefa não encontrada!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione uma tarefa!");
            }
        });

        btnVoltar.addActionListener(e -> {
            frame.setContentPane(new TelaGerenciadorTarefas(frame, sistema));
            frame.revalidate();
            frame.repaint();
        });

        // Carregar dados reais
        carregarTarefas(dataStr);
    }

    private void carregarTarefas(String dataStr) {
        List<Tarefa> tarefas;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Determinar quais tarefas carregar baseado no título
        if (tituloTela.equals("Tarefas por Data") && dataStr != null) {
            try {
                LocalDate dataSelecionada = LocalDate.parse(dataStr, formatter);
                tarefas = sistema.listarTarefasPorData(dataSelecionada);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Data inválida! Mostrando todas as tarefas.");
                tarefas = sistema.listarTodasTarefas();
            }
        } else if (tituloTela.equals("Tarefas Críticas")) {
            tarefas = sistema.listarTarefasCriticas();
        } else {
            tarefas = sistema.listarTodasTarefas();
        }

        // Limpar tabela
        modelo.setRowCount(0);

        // Adicionar tarefas à tabela
        for (Tarefa tarefa : tarefas) {
            modelo.addRow(new Object[] {
                    tarefa.getTitulo(),
                    tarefa.getDescricao(),
                    tarefa.getDeadline().format(formatter),
                    tarefa.getPrioridade(),
                    String.format("%.1f%%", tarefa.getPercentual())
            });
        }
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    @Override
    public void removeNotify() {
        sistema.getTarefaService().removerObservador(this);
        super.removeNotify();
    }

    @Override
    public void atualizar(Object mensagem) {
        SwingUtilities.invokeLater(() -> {
            carregarTarefas(this.dataFiltro);
        });
    }

    private final String dataFiltro;
}