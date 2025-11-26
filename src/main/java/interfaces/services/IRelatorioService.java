package interfaces.services;

import modelo.Tarefa;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para serviços de geração de relatórios.
 * <p>
 * Define operações para geração de relatórios em diferentes formatos,
 * seguindo o princípio ISP (Interface Segregation Principle).
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.0
 * @since 2.0
 */
public interface IRelatorioService {
    /**
     * Gera relatório em formato PDF.
     * 
     * @param tarefas lista de tarefas para incluir no relatório
     * @param data data de referência do relatório
     * @return true se o relatório foi gerado com sucesso, false caso contrário
     */
    boolean gerarPDF(List<Tarefa> tarefas, LocalDate data);
    
    /**
     * Gera relatório em formato Excel.
     * 
     * @param tarefas lista de tarefas para incluir no relatório
     * @param mes mês de referência
     * @param ano ano de referência
     * @return true se o relatório foi gerado com sucesso, false caso contrário
     */
    boolean gerarExcel(List<Tarefa> tarefas, int mes, int ano);
}