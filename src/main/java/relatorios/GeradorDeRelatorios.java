package relatorios;

import interfaces.services.IRelatorioService;
import java.time.LocalDate;
import java.util.List;
import modelo.Tarefa;

/**
 * Service responsável pela geração de relatórios.
 * <p>
 * Atua agora como um Client/Factory para o padrão Template Method.
 * Delega a geração real para as subclasses de {@link RelatorioTemplate}.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 3.0 (Refatorado com Template Method)
 */
public class GeradorDeRelatorios implements IRelatorioService {

    @Override
    public boolean gerarPDF(List<Tarefa> tarefas, LocalDate data) {
        RelatorioTemplate relatorio = new RelatorioPDF(data);
        return relatorio.gerarRelatorio(tarefas, "relatorio.pdf");
    }

    @Override
    public boolean gerarExcel(List<Tarefa> tarefas, int mes, int ano) {
        RelatorioTemplate relatorio = new RelatorioCSV();
        return relatorio.gerarRelatorio(tarefas, "relatorio_mensal.csv");
    }
}
