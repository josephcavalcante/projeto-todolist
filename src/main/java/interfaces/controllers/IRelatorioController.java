package interfaces.controllers;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

public interface IRelatorioController {
    boolean gerarRelatorioPDF(List<Tarefa> tarefas, LocalDate data);

    boolean gerarRelatorioExcel(List<Tarefa> tarefas, int mes, int ano);

    boolean enviarRelatorioEmail(List<Tarefa> tarefas, String emailDestino, LocalDate data);
}
