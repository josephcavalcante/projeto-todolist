package servicos;

import interfaces.IRelatorioService;
import modelo.Tarefa;
import relatorios.GeradorDeRelatorios;
import java.time.LocalDate;
import java.util.List;

// service que cuida so dos relatorios
// separei da classe principal pra ficar mais organizado
public class RelatorioService implements IRelatorioService {
    
    @Override
    public boolean gerarPDF(List<Tarefa> tarefas, LocalDate data) {
        try {
            GeradorDeRelatorios.gerarRelatorioPDF(tarefas, data);
            return true;
        } catch (Exception ex) {
            System.out.println("Erro ao gerar PDF: " + ex.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean gerarExcel(List<Tarefa> tarefas, int mes, int ano) {
        try {
            GeradorDeRelatorios.gerarRelatorioExcel(tarefas, mes, ano);
            return true;
        } catch (Exception ex) {
            System.out.println("Erro ao gerar Excel: " + ex.getMessage());
            return false;
        }
    }
}