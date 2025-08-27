package interfaces;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para serviços de relatório
 * Princípio OCP: Extensível para novos tipos de relatório
 */
public interface IRelatorioService {
    boolean gerarPDF(List<Tarefa> tarefas, LocalDate data);
    boolean gerarExcel(List<Tarefa> tarefas, int mes, int ano);
}