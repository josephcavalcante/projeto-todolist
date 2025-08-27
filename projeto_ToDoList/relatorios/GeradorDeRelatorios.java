package relatorios;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import modelo.Tarefa;

// criação de relatórios em PDF e Excel
public class GeradorDeRelatorios {
    
    // geração de relatório PDF diário
    public static void gerarRelatorioPDF(List<Tarefa> tarefas, LocalDate data) {
        Document documentoPDF = new Document();
        try {
            // configuração do arquivo PDF
            PdfWriter.getInstance(documentoPDF, new FileOutputStream("relatorio.pdf"));
            documentoPDF.open();
            documentoPDF.add(new Paragraph("Relatório de Tarefas do dia: " + data));
            documentoPDF.add(new Paragraph(" "));

            // verificação de existência de tarefas
            if (tarefas.isEmpty()) {
                documentoPDF.add(new Paragraph("Nenhuma tarefa encontrada para este dia."));
            } else {
                // iteração sobre as tarefas
                for (Tarefa item : tarefas) {
                    documentoPDF.add(new Paragraph("Título: " + item.getTitulo()));
                    documentoPDF.add(new Paragraph("Descrição: " + item.getDescricao()));
                    documentoPDF.add(new Paragraph("Deadline: " + item.getDeadline()));
                    documentoPDF.add(new Paragraph("Prioridade: " + item.getPrioridade()));
                    documentoPDF.add(new Paragraph("Percentual: " + item.getPercentual() + "%"));
                    documentoPDF.add(new Paragraph(" "));
                }
            }
        } catch (DocumentException | IOException erro) {
            erro.printStackTrace();
        } finally {
            documentoPDF.close();
        }
    }

    // criação de planilha Excel (formato CSV)
    public static void gerarRelatorioExcel(List<Tarefa> tarefas, int mes, int ano) {
        // implementação simples usando CSV
        try (FileOutputStream arquivoSaida = new FileOutputStream("relatorio_mensal.csv")) {
            StringBuilder conteudoCSV = new StringBuilder();
            conteudoCSV.append("Título,Descrição,Deadline,Prioridade,Percentual,Status\n");
            
            // processamento de cada tarefa
            for (Tarefa itemTarefa : tarefas) {
                String situacao = itemTarefa.getPercentual() >= 100.0 ? "CONCLUÍDA" : "PENDENTE"; // 100.0 explicito
                conteudoCSV.append(String.format("\"%s\",\"%s\",%s,%d,%.1f,%s\n",
                    itemTarefa.getTitulo(),
                    itemTarefa.getDescricao(),
                    itemTarefa.getDeadline(),
                    itemTarefa.getPrioridade(),
                    itemTarefa.getPercentual(),
                    situacao));
            }
            
            // gravação do conteúdo
            arquivoSaida.write(conteudoCSV.toString().getBytes("UTF-8"));
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }
} 