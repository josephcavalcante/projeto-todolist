package telas;

import java.awt.*;
import javax.swing.*;

import modelo.Tarefa;
import negocio.ToDoList;

public class TelaGerenciadorTarefas extends JPanel {

    private TelaPrincipal frame;
    private ToDoList sistema;

    public TelaGerenciadorTarefas(TelaPrincipal frame, ToDoList sistema) {
        this.frame = frame;
        this.sistema = sistema;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 250, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Gerenciador de Tarefas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(60, 90, 170));
        add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new GridLayout(0, 1, 15, 15));
        botoes.setBackground(new Color(240, 250, 255));

        JButton btnCadastrar = new JButton("Cadastrar Nova Tarefa");
        JButton btnListarTodas = new JButton("Listar Todas as Tarefas");
        JButton btnListarPorData = new JButton("Listar Tarefas por Data");
        JButton btnListarCriticas = new JButton("Listar Tarefas Críticas");
        JButton btnEditar = new JButton("Editar Tarefa");
        JButton btnExcluir = new JButton("Excluir Tarefa");
        JButton btnSubtarefas = new JButton("Gerenciar Subtarefas");
        JButton btnVoltar = new JButton("Voltar");

        btnCadastrar.setFont(new Font("Arial", Font.PLAIN, 18));
        btnListarTodas.setFont(new Font("Arial", Font.PLAIN, 18));
        btnListarPorData.setFont(new Font("Arial", Font.PLAIN, 18));
        btnListarCriticas.setFont(new Font("Arial", Font.PLAIN, 18));
        btnEditar.setFont(new Font("Arial", Font.PLAIN, 18));
        btnExcluir.setFont(new Font("Arial", Font.PLAIN, 18));
        btnSubtarefas.setFont(new Font("Arial", Font.PLAIN, 18));
        btnVoltar.setFont(new Font("Arial", Font.PLAIN, 18));

        botoes.add(btnCadastrar);
        botoes.add(btnListarTodas);
        botoes.add(btnListarPorData);
        botoes.add(btnListarCriticas);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        botoes.add(btnSubtarefas);
        botoes.add(btnVoltar);

        add(botoes, BorderLayout.CENTER);

        // Ações dos botões
        btnCadastrar.addActionListener(e -> {
            frame.setContentPane(new TelaCadastroTarefa(frame, sistema));
            frame.revalidate();
            frame.repaint();
        });
        btnListarTodas.addActionListener(e -> {
            frame.setContentPane(new TelaListarTarefas(frame, sistema, "Todas as Tarefas"));
            frame.revalidate();
            frame.repaint();
        });
        btnListarPorData.addActionListener(e -> {
            String dataStr = JOptionPane.showInputDialog(frame,
                    "Digite a data (dd/MM/yyyy):",
                    java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            if (dataStr != null) {
                frame.setContentPane(new TelaListarTarefas(frame, sistema, "Tarefas por Data", dataStr));
                frame.revalidate();
                frame.repaint();
            }
        });
        btnListarCriticas.addActionListener(e -> {
            frame.setContentPane(new TelaListarTarefas(frame, sistema, "Tarefas Críticas"));
            frame.revalidate();
            frame.repaint();
        });
        btnEditar.addActionListener(e -> {
            String tituloEditar = JOptionPane.showInputDialog(frame, "Digite o título da tarefa a editar:");
            if (tituloEditar != null && !tituloEditar.trim().isEmpty()) {
                Tarefa tarefa = sistema.buscarTarefaPorTitulo(tituloEditar);
                if (tarefa != null) {
                    frame.setContentPane(new TelaEditarTarefa(frame, sistema, tarefa));
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Tarefa não encontrada!");
                }
            }
        });
        btnExcluir.addActionListener(e -> {
            String tituloExcluir = JOptionPane.showInputDialog(frame, "Digite o título da tarefa a excluir:");
            if (tituloExcluir != null && !tituloExcluir.trim().isEmpty()) {
                int confirmacao = JOptionPane.showConfirmDialog(frame,
                        "Tem certeza que deseja excluir a tarefa '" + tituloExcluir + "'?",
                        "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    if (sistema.getTarefaService().excluir(tituloExcluir)) {
                        JOptionPane.showMessageDialog(frame, "Tarefa excluída com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Tarefa não encontrada!");
                    }
                }
            }
        });
        btnSubtarefas.addActionListener(e -> {
            // seleção direta da tarefa para gerenciar subtarefas
            String tituloTarefa = JOptionPane.showInputDialog(frame, "Digite o título da tarefa:");
            if (tituloTarefa != null && !tituloTarefa.trim().isEmpty()) {
                modelo.Tarefa tarefa = sistema.buscarTarefaPorTitulo(tituloTarefa);
                if (tarefa != null) {
                    frame.setContentPane(new TelaSubtarefas(frame, sistema, tarefa));
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Tarefa não encontrada!");
                }
            }
        });
        btnVoltar.addActionListener(e -> {
            frame.setContentPane(frame.criarPainelPrincipal());
            frame.revalidate();
            frame.repaint();
        });
    }
}