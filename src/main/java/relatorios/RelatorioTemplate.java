package relatorios;

import java.util.List;
import modelo.Tarefa;

/**
 * Template Method para geração de relatórios.
 * Define o esqueleto do algoritmo de exportação.
 */
public abstract class RelatorioTemplate {

    /**
     * Define o fluxo que não pode ser alterado.
     */
    public final boolean gerarRelatorio(List<Tarefa> tarefas, String caminhoArquivo) {
        try {
            abrirArquivo(caminhoArquivo);
            escreverCabecalho();

            if (tarefas.isEmpty()) {
                escreverCorpoVazio();
            } else {
                for (Tarefa tarefa : tarefas) {
                    escreverTarefa(tarefa);
                }
            }

            escreverRodape();
            fecharArquivo();
            System.out.println("Relatório gerado em: " + caminhoArquivo);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
            tratarErro(e);
            return false;
        }
    }

    // Hooks que as subclasses devem implementar
    protected abstract void abrirArquivo(String caminho) throws Exception;

    protected abstract void escreverCabecalho() throws Exception;

    protected abstract void escreverTarefa(Tarefa tarefa) throws Exception;

    protected abstract void fecharArquivo() throws Exception;

    // Hooks opcionais
    protected void escreverCorpoVazio() throws Exception {
        // Padrão: não faz nada ou loga
    }

    protected void escreverRodape() throws Exception {
        // Padrão: não faz nada
    }

    protected void tratarErro(Exception e) {
        // Padrão: apenas logou acima, pode ser sobrescrito para limpeza
    }
}
