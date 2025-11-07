package controle.services;

import java.time.LocalDate;
import java.util.List;

import interfaces.IRelatorioService;
import modelo.Tarefa;
import relatorios.GeradorDeRelatorios;

/**
 * Service responsável pela geração de relatórios.
 * <p>
 * Centraliza a lógica de geração de relatórios em diferentes formatos,
 * aplicando o princípio SRP (Single Responsibility Principle).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
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