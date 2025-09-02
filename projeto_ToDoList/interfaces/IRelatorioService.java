package interfaces;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

// interface pros relatorios
// assim fica mais facil adicionar novos tipos depois
public interface IRelatorioService {
    boolean gerarPDF(List<Tarefa> tarefas, LocalDate data);
    boolean gerarExcel(List<Tarefa> tarefas, int mes, int ano);
}