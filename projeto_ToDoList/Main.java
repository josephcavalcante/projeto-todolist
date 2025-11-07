import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import modelo.Tarefa;
import negocio.ToDoList;
import modelo.Subtarefa;

/**
 * Classe principal para testar o sistema To Do List.
 * Demonstra as funcionalidades do backend.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA TO DO LIST ===");
        System.out.println("Iniciando sistema...\n");
        
        // Inicializa o sistema
        ToDoList toDoList = new ToDoList();
        
        System.out.println("Usuário: " + toDoList.obterUsuario().getNome());
        System.out.println("E-mail: " + toDoList.obterUsuario().getEmail() + "\n");
        
        // Menu principal
        Scanner scanner = new Scanner(System.in);
        while (true) {
            exibirMenu();
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1":
                    listarTodasTarefas(toDoList);
                    break;
                case "2":
                    listarTarefasCriticas(toDoList);
                    break;
                case "3":
                    listarTarefasPorData(toDoList, scanner);
                    break;
                case "4":
                    gerarRelatorioPDF(toDoList, scanner);
                    break;
                case "5":
                    gerarRelatorioExcel(toDoList);
                    break;
                case "6":
                    enviarRelatorioEmail(toDoList, scanner);
                    break;
                case "7":
                    gerenciarSubtarefas(toDoList, scanner);
                    break;
                case "0":
                    System.out.println("Saindo do sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!\n");
            }
        }
    }
    

    
    /**
     * Exibe o menu principal.
     */
    private static void exibirMenu() {
        System.out.println("=== MENU PRINCIPAL ===");
        System.out.println("1 - Listar todas as tarefas");
        System.out.println("2 - Listar tarefas críticas");
        System.out.println("3 - Listar tarefas por data");
        System.out.println("4 - Gerar relatório PDF");
        System.out.println("5 - Gerar relatório Excel");
        System.out.println("6 - Enviar relatório por e-mail");
        System.out.println("7 - Gerenciar subtarefas");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    /**
     * Lista todas as tarefas cadastradas.
     */
    private static void listarTodasTarefas(ToDoList toDoList) {
        System.out.println("\n=== TODAS AS TAREFAS ===");
        List<Tarefa> tarefas = toDoList.listarTodasTarefas();
        
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            for (int i = 0; i < tarefas.size(); i++) {
                Tarefa t = tarefas.get(i);
                System.out.println((i + 1) + ". " + t.getTitulo());
                System.out.println("   Descrição: " + t.getDescricao());
                System.out.println("   Deadline: " + t.getDeadline());
                System.out.println("   Prioridade: " + t.getPrioridade());
                System.out.println("   Percentual: " + t.getPercentual() + "%");
                System.out.println();
            }
        }
        System.out.println();
    }
    
    /**
     * Lista tarefas críticas.
     */
    private static void listarTarefasCriticas(ToDoList toDoList) {
        System.out.println("\n=== TAREFAS CRÍTICAS ===");
        List<Tarefa> tarefasCriticas = toDoList.listarTarefasCriticas();
        
        if (tarefasCriticas.isEmpty()) {
            System.out.println("Nenhuma tarefa crítica encontrada.");
        } else {
            for (Tarefa tarefa : tarefasCriticas) {
                System.out.println("⚠️  " + tarefa.getTitulo());
                System.out.println("   Deadline: " + tarefa.getDeadline());
                System.out.println("   Prioridade: " + tarefa.getPrioridade());
                System.out.println();
            }
        }
        System.out.println();
    }
    
    /**
     * Lista tarefas por data específica.
     */
    private static void listarTarefasPorData(ToDoList toDoList, Scanner scanner) {
        System.out.print("Digite a data (AAAA-MM-DD): ");
        String dataStr = scanner.nextLine();
        
        try {
            LocalDate data = LocalDate.parse(dataStr);
            List<Tarefa> tarefas = toDoList.listarTarefasPorData(data);
            
            System.out.println("\n=== TAREFAS DO DIA " + data + " ===");
            if (tarefas.isEmpty()) {
                System.out.println("Nenhuma tarefa encontrada para esta data.");
            } else {
                for (Tarefa tarefa : tarefas) {
                    System.out.println("• " + tarefa.getTitulo());
                    System.out.println("  " + tarefa.getDescricao());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Data inválida! Use o formato AAAA-MM-DD");
        }
        System.out.println();
    }
    
    /**
     * Gera relatório PDF.
     */
    private static void gerarRelatorioPDF(ToDoList toDoList, Scanner scanner) {
        System.out.print("Digite a data para o relatório (AAAA-MM-DD): ");
        String dataStr = scanner.nextLine();
        
        try {
            LocalDate data = LocalDate.parse(dataStr);
            boolean sucesso = toDoList.gerarRelatorioPDF(data);
            
            if (sucesso) {
                System.out.println("Relatório PDF gerado com sucesso!");
                System.out.println("Arquivo: relatorio.pdf");
            } else {
                System.out.println("Erro ao gerar relatório PDF.");
            }
        } catch (Exception e) {
            System.out.println("Data inválida!");
        }
        System.out.println();
    }
    
    /**
     * Gera relatório Excel.
     */
    private static void gerarRelatorioExcel(ToDoList toDoList) {
        System.out.println("Gerando relatório Excel...");
        boolean sucesso = toDoList.gerarRelatorioExcel(12, 2024); // Dezembro 2024
        
        if (sucesso) {
            System.out.println("Relatório Excel gerado com sucesso!");
            System.out.println("Arquivo: relatorio_mensal.csv");
        } else {
            System.out.println("Erro ao gerar relatório Excel.");
        }
        System.out.println();
    }
    
    /**
     * Envia relatório por e-mail.
     */
    private static void enviarRelatorioEmail(ToDoList toDoList, Scanner scanner) {
        System.out.print("Digite a data para o relatório (AAAA-MM-DD): ");
        String dataStr = scanner.nextLine();
        
        try {
            LocalDate data = LocalDate.parse(dataStr);
            boolean sucesso = toDoList.enviarRelatorioEmail(data);
            
            if (sucesso) {
                System.out.println("E-mail enviado com sucesso!");
                System.out.println("Destinatário: " + toDoList.obterUsuario().getEmail());
            } else {
                System.out.println("Erro ao enviar e-mail.");
            }
        } catch (Exception e) {
            System.out.println("Data inválida!");
        }
        System.out.println();
    }
    
    /**
     * Gerencia subtarefas de uma tarefa.
     */
    private static void gerenciarSubtarefas(ToDoList toDoList, Scanner scanner) {
        System.out.println("=== GERENCIAR SUBTAREFAS ===");
        List<Tarefa> tarefas = toDoList.listarTodasTarefas();
        
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa disponível.");
            return;
        }
        
        System.out.println("Tarefas disponíveis:");
        for (int i = 0; i < tarefas.size(); i++) {
            System.out.println((i + 1) + ". " + tarefas.get(i).getTitulo());
        }
        
        System.out.print("Escolha uma tarefa (número): ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine()) - 1;
            if (escolha >= 0 && escolha < tarefas.size()) {
                Tarefa tarefa = tarefas.get(escolha);
                System.out.println("Tarefa selecionada: " + tarefa.getTitulo());
                
                // Lista subtarefas existentes
                List<Subtarefa> subtarefas = toDoList.listarSubtarefas(tarefa);
                if (subtarefas.isEmpty()) {
                    System.out.println("Esta tarefa não possui subtarefas.");
                } else {
                    System.out.println("Subtarefas:");
                    for (Subtarefa sub : subtarefas) {
                        System.out.println("- " + sub.getTitulo() + " (" + sub.getPercentual() + "%)");
                    }
                }
                System.out.println("Percentual da tarefa: " + tarefa.getPercentual() + "%");
            } else {
                System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Número inválido!");
        }
        System.out.println();
    }
} 