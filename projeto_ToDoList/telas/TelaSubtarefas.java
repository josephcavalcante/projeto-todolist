package telas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import modelo.Subtarefa;
import modelo.Tarefa;
import negocio.ToDoList;

public class TelaSubtarefas extends JPanel {
    private TelaPrincipal janelaPai;
    private ToDoList app;
    private Tarefa tarefaPrincipal;
    private JTable tabelaSubtarefas;
    private DefaultTableModel modeloTabela;

    public TelaSubtarefas(TelaPrincipal frame, ToDoList sistema, Tarefa tarefa) {
        this.janelaPai = frame;
        this.app = sistema;
        this.tarefaPrincipal = tarefa;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // cabecalho com nome da tarefa
        JLabel cabecalho = new JLabel("Subtarefas - " + tarefaPrincipal.getTitulo(), JLabel.CENTER);
        cabecalho.setFont(new Font("Arial", Font.BOLD, 28));
        cabecalho.setForeground(new Color(60, 90, 170));
        add(cabecalho, BorderLayout.NORTH);

        // criando a tabela das subtarefas
        String[] nomesColunas = {"Título", "Descrição", "Percentual"};
        modeloTabela = new DefaultTableModel(nomesColunas, 0);
        tabelaSubtarefas = new JTable(modeloTabela);
        tabelaSubtarefas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaSubtarefas.setRowHeight(25); // altura das linhas
        
        JScrollPane scroll = new JScrollPane(tabelaSubtarefas);
        scroll.setPreferredSize(new Dimension(0, 300));
        add(scroll, BorderLayout.CENTER);

        // area dos botoes
        JPanel areaBotoes = new JPanel(new GridLayout(1, 4, 10, 10));
        areaBotoes.setBackground(new Color(245, 245, 255));
        areaBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton botAdd = new JButton("Adicionar");
        JButton botEdit = new JButton("Editar");
        JButton botDel = new JButton("Excluir");
        JButton botBack = new JButton("Voltar");

        // configurando fonte dos botoes
        Font fonteBotao = new Font("Arial", Font.PLAIN, 16);
        botAdd.setFont(fonteBotao);
        botEdit.setFont(fonteBotao);
        botDel.setFont(fonteBotao);
        botBack.setFont(fonteBotao);

        areaBotoes.add(botAdd);
        areaBotoes.add(botEdit);
        areaBotoes.add(botDel);
        areaBotoes.add(botBack);

        add(areaBotoes, BorderLayout.SOUTH);

        // eventos dos botoes
        botAdd.addActionListener(e -> adicionarSubtarefa());
        botEdit.addActionListener(e -> editarSubtarefaSelecionada());
        botDel.addActionListener(e -> excluirSubtarefaSelecionada());
        botBack.addActionListener(e -> voltarTela());

        // carrega as subtarefas na tabela
        carregarSubtarefas();
    }

    private void carregarSubtarefas() {
        // busca as subtarefas da tarefa atual
        List<Subtarefa> listaSubs = app.listarSubtarefas(tarefaPrincipal);
        modeloTabela.setRowCount(0); // limpa a tabela
        
        // adiciona cada subtarefa na tabela
        for (Subtarefa sub : listaSubs) {
            modeloTabela.addRow(new Object[]{
                sub.getTitulo(),
                sub.getDescricao(),
                String.format("%.1f%%", sub.getPercentual())
            });
        }
    }

    private void adicionarSubtarefa() {
        // cria o formulario vazio
        JPanel form = criarFormularioSubtarefa("", "", 0.0);
        int resposta = JOptionPane.showConfirmDialog(janelaPai, form, "Nova Subtarefa", JOptionPane.OK_CANCEL_OPTION);
        
        if (resposta == JOptionPane.OK_OPTION) {
            // pega os dados do formulario
            String tit = ((JTextField) form.getComponent(1)).getText().trim();
            String desc = ((JTextField) form.getComponent(3)).getText().trim();
            double perc = (Double) ((JSpinner) form.getComponent(5)).getValue();
            
            // tenta adicionar
            if (app.obterSubtarefaService().adicionar(tarefaPrincipal.getTitulo(), tit, desc, perc)) {
                app.salvarDados();
                carregarSubtarefas(); // atualiza a tabela
                JOptionPane.showMessageDialog(janelaPai, "Subtarefa adicionada!");
            } else {
                JOptionPane.showMessageDialog(janelaPai, "Erro: Título é obrigatório!");
            }
        }
    }

    private void editarSubtarefa(int linha) {
        String tituloOriginal = (String) modeloTabela.getValueAt(linha, 0);
        String descricaoOriginal = (String) modeloTabela.getValueAt(linha, 1);
        String percentualStr = modeloTabela.getValueAt(linha, 2).toString().replace("%", "").replace(",", ".");
        double percentualOriginal;
        try {
            percentualOriginal = Double.parseDouble(percentualStr);
        } catch (NumberFormatException e) {
            percentualOriginal = 0.0;
        }
        
        JPanel painel = criarFormularioSubtarefa(tituloOriginal, descricaoOriginal, percentualOriginal);
        int resultado = JOptionPane.showConfirmDialog(janelaPai, painel, "Editar Subtarefa", JOptionPane.OK_CANCEL_OPTION);
        
        if (resultado == JOptionPane.OK_OPTION) {
            String novoTitulo = ((JTextField) painel.getComponent(1)).getText().trim();
            String novaDescricao = ((JTextField) painel.getComponent(3)).getText().trim();
            double novoPercentual = (Double) ((JSpinner) painel.getComponent(5)).getValue();
            
            if (app.obterSubtarefaService().editar(tarefaPrincipal.getTitulo(), tituloOriginal, novoTitulo, novaDescricao, novoPercentual)) {
                app.salvarDados();
                carregarSubtarefas();
                JOptionPane.showMessageDialog(janelaPai, "Subtarefa editada!");
            } else {
                JOptionPane.showMessageDialog(janelaPai, "Erro: Título é obrigatório!");
            }
        }
    }

    private void editarSubtarefaSelecionada() {
        int linhaSelecionada = tabelaSubtarefas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            editarSubtarefa(linhaSelecionada);
        } else {
            JOptionPane.showMessageDialog(janelaPai, "Selecione uma subtarefa!");
        }
    }

    private void excluirSubtarefaSelecionada() {
        int linha = tabelaSubtarefas.getSelectedRow();
        if (linha >= 0) {
            String tituloSub = (String) modeloTabela.getValueAt(linha, 0);
            if (confirmarExclusao(tituloSub)) {
                excluirSubtarefa(tituloSub);
            }
        } else {
            JOptionPane.showMessageDialog(janelaPai, "Selecione uma subtarefa!");
        }
    }

    private boolean confirmarExclusao(String titulo) {
        return JOptionPane.showConfirmDialog(janelaPai, 
            "Excluir subtarefa: " + titulo + "?", 
            "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void excluirSubtarefa(String titulo) {
        if (app.obterSubtarefaService().remover(tarefaPrincipal.getTitulo(), titulo)) {
            app.salvarDados();
            carregarSubtarefas();
            JOptionPane.showMessageDialog(janelaPai, "Subtarefa excluída!");
        } else {
            JOptionPane.showMessageDialog(janelaPai, "Erro ao excluir subtarefa!");
        }
    }

    private void voltarTela() {
        // volta sempre pro gerenciador principal
        janelaPai.setContentPane(new TelaGerenciadorTarefas(janelaPai, app));
        janelaPai.revalidate();
        janelaPai.repaint();
    }

    private JPanel criarFormularioSubtarefa(String titulo, String descricao, double percentual) {
        JPanel painel = new JPanel(new GridLayout(0, 1, 8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField txtTitulo = new JTextField(titulo);
        txtTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField txtDescricao = new JTextField(descricao);
        txtDescricao.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel lblPercentual = new JLabel("Percentual (0-100):");
        lblPercentual.setFont(new Font("Arial", Font.BOLD, 14));
        JSpinner spnPercentual = new JSpinner(new SpinnerNumberModel(percentual, 0.0, 100.0, 1.0));
        spnPercentual.setFont(new Font("Arial", Font.PLAIN, 14));
        
        painel.add(lblTitulo);
        painel.add(txtTitulo);
        painel.add(lblDescricao);
        painel.add(txtDescricao);
        painel.add(lblPercentual);
        painel.add(spnPercentual);
        
        return painel;
    }
}