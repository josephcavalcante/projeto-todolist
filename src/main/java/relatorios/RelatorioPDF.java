package relatorios;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import modelo.Tarefa;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class RelatorioPDF extends RelatorioTemplate {

    private Document documentoPDF;
    private LocalDate dataRelatorio;

    public RelatorioPDF(LocalDate data) {
        this.dataRelatorio = data;
    }

    @Override
    protected void abrirArquivo(String caminho) throws Exception {
        documentoPDF = new Document();
        PdfWriter.getInstance(documentoPDF, new FileOutputStream(caminho));
        documentoPDF.open();
    }

    @Override
    protected void escreverCabecalho() throws Exception {
        documentoPDF.add(new Paragraph("Relatório de Tarefas do dia: " + dataRelatorio));
        documentoPDF.add(new Paragraph(" ")); // Linha em branco
    }

    @Override
    protected void escreverCorpoVazio() throws Exception {
        documentoPDF.add(new Paragraph("Nenhuma tarefa encontrada para este dia."));
    }

    @Override
    protected void escreverTarefa(Tarefa tarefa) throws Exception {
        documentoPDF.add(new Paragraph("Título: " + tarefa.getTitulo()));
        documentoPDF.add(new Paragraph("Descrição: " + tarefa.getDescricao()));
        documentoPDF.add(new Paragraph("Deadline: " + tarefa.getDeadline()));
        documentoPDF.add(new Paragraph("Prioridade: " + tarefa.getPrioridade()));
        documentoPDF.add(new Paragraph("Percentual: " + tarefa.getPercentual() + "%"));
        documentoPDF.add(new Paragraph("--------------------------------------------------"));
    }

    @Override
    protected void fecharArquivo() throws Exception {
        if (documentoPDF != null) {
            documentoPDF.close();
        }
    }
}
