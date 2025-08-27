package telas;
import javax.swing.*;
import java.awt.*;
import controle.ToDoList;

// tela para geração de relatórios
public class TelaRelatorios extends JPanel {
    private TelaPrincipal janelaPai;
    private ToDoList sistemaApp;

    public TelaRelatorios(TelaPrincipal frame, ToDoList sistema) {
        this.janelaPai = frame;
        this.sistemaApp = sistema; // referência do sistema principal
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // cabeçalho da tela
        JLabel cabecalho = new JLabel("Relatórios de Tarefas", JLabel.CENTER);
        cabecalho.setFont(new Font("Arial", Font.BOLD, 28));
        cabecalho.setForeground(new Color(60, 90, 170));
        add(cabecalho, BorderLayout.NORTH);

        // área dos botões de ação
        JPanel painelBotoes = new JPanel(new GridLayout(0, 1, 15, 15));
        painelBotoes.setBackground(new Color(245, 245, 255));

        JButton botaoPDF = new JButton("Exportar Tarefas do Dia (PDF)");
        JButton botaoEmail = new JButton("Enviar Tarefas do Dia por E-mail");
        JButton botaoExcel = new JButton("Exportar Tarefas do Mês (Excel)");
        JButton botaoVoltar = new JButton("Voltar");

        // configuração da fonte dos botões
        Font fonteBotao = new Font("Arial", Font.PLAIN, 18);
        botaoPDF.setFont(fonteBotao);
        botaoEmail.setFont(fonteBotao);
        botaoExcel.setFont(fonteBotao);
        botaoVoltar.setFont(fonteBotao);

        painelBotoes.add(botaoPDF);
        painelBotoes.add(botaoEmail);
        painelBotoes.add(botaoExcel);
        painelBotoes.add(botaoVoltar);

        add(painelBotoes, BorderLayout.CENTER);

        // associação dos eventos
        botaoPDF.addActionListener(e -> exportarPDF());
        botaoEmail.addActionListener(e -> enviarEmail());
        botaoExcel.addActionListener(e -> exportarExcel());
        botaoVoltar.addActionListener(e -> {
            // retorno ao menu principal
            janelaPai.setContentPane(janelaPai.criarPainelPrincipal());
            janelaPai.revalidate();
            janelaPai.repaint();
        });
    }

    // geração de relatório em PDF
    private void exportarPDF() {
        String textoData = JOptionPane.showInputDialog(janelaPai, "Digite a data (dd/MM/yyyy):");
        if (textoData != null && !textoData.trim().isEmpty()) {
            try {
                // formatador para conversão da data
                java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.LocalDate dataEscolhida = java.time.LocalDate.parse(textoData.trim(), formatador);
                
                // tentativa de geração do PDF
                if (sistemaApp.gerarRelatorioPDF(dataEscolhida)) {
                    JOptionPane.showMessageDialog(janelaPai, 
                        "Relatório PDF das tarefas do dia " + textoData + " gerado com sucesso!\n" +
                        "Arquivo salvo em: relatorio_" + textoData.replace("/", "") + ".pdf");
                } else {
                    JOptionPane.showMessageDialog(janelaPai, "Erro ao gerar relatório PDF!");
                }
            } catch (Exception erro) {
                JOptionPane.showMessageDialog(janelaPai, "Data inválida! Use o formato dd/MM/yyyy");
            }
        }
    }

    // envio de relatório por email
    private void enviarEmail() {
        String inputData = JOptionPane.showInputDialog(janelaPai, "Digite a data (dd/MM/yyyy):");
        if (inputData != null && !inputData.trim().isEmpty()) {
            try {
                // processamento da data informada
                java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.LocalDate dataRelatorio = java.time.LocalDate.parse(inputData.trim(), formatador);
                
                // tentativa de envio do email
                if (sistemaApp.enviarRelatorioEmail(dataRelatorio)) {
                    JOptionPane.showMessageDialog(janelaPai, 
                        "Relatório das tarefas do dia " + inputData + " enviado por e-mail!\n" +
                        "Enviado para: " + sistemaApp.obterUsuario().getEmail());
                } else {
                    JOptionPane.showMessageDialog(janelaPai, "Erro ao enviar e-mail!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(janelaPai, "Data inválida! Use o formato dd/MM/yyyy");
            }
        }
    }

    // criação de planilha Excel
    private void exportarExcel() {
        String periodoStr = JOptionPane.showInputDialog(janelaPai, "Digite o mês/ano (MM/yyyy):");
        if (periodoStr != null && !periodoStr.trim().isEmpty()) {
            try {
                // separação do mês e ano
                String[] componentesPeriodo = periodoStr.trim().split("/");
                if (componentesPeriodo.length != 2) throw new Exception("Formato inválido");
                
                int mesEscolhido = Integer.parseInt(componentesPeriodo[0]);
                int anoEscolhido = Integer.parseInt(componentesPeriodo[1]);
                
                // validação do mês
                if (mesEscolhido < 1 || mesEscolhido > 12) throw new Exception("Mês inválido");
                
                // processamento da geração
                if (sistemaApp.gerarRelatorioExcel(mesEscolhido, anoEscolhido)) {
                    JOptionPane.showMessageDialog(janelaPai, 
                        "Relatório Excel das tarefas do mês " + periodoStr + " gerado com sucesso!\n" +
                        "Arquivo salvo em: relatorio_" + periodoStr.replace("/", "") + ".csv");
                } else {
                    JOptionPane.showMessageDialog(janelaPai, "Erro ao gerar relatório Excel!");
                }
            } catch (Exception erro) {
                JOptionPane.showMessageDialog(janelaPai, "Formato inválido! Use MM/yyyy (ex: 12/2024)");
            }
        }
    }
}
