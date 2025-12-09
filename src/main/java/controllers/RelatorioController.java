package controllers;

import interfaces.controllers.IRelatorioController;
import interfaces.services.IRelatorioService;
import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;
import comunicacao.Mensageiro;

public class RelatorioController implements IRelatorioController {
    private final IRelatorioService relatorioService;

    public RelatorioController(IRelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @Override
    public boolean gerarRelatorioPDF(List<Tarefa> tarefas, LocalDate data) {
        return relatorioService.gerarPDF(tarefas, data);
    }

    @Override
    public boolean gerarRelatorioExcel(List<Tarefa> tarefas, int mes, int ano) {
        return relatorioService.gerarExcel(tarefas, mes, ano);
    }

    @Override
    public boolean enviarRelatorioEmail(List<Tarefa> tarefas, String emailDestino, LocalDate data) {
        try {
            boolean gerou = relatorioService.gerarPDF(tarefas, data);
            if (!gerou)
                return false;

            Mensageiro.enviarEmail(emailDestino, "Relatório do dia " + data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
