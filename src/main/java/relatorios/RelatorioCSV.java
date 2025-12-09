package relatorios;

import modelo.Tarefa;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class RelatorioCSV extends RelatorioTemplate {

    private OutputStreamWriter escritor;
    private StringBuilder buffer;

    @Override
    protected void abrirArquivo(String caminho) throws Exception {
        // Usa OutputStreamWriter para UTF-8
        escritor = new OutputStreamWriter(new FileOutputStream(caminho), StandardCharsets.UTF_8);
        buffer = new StringBuilder();

        // Adiciona BOM (Byte Order Mark) para o Excel reconhecer UTF-8
        buffer.append("\uFEFF");
    }

    @Override
    protected void escreverCabecalho() throws Exception {
        // Cabeçalho do CSV
        buffer.append("Título;Descrição;Deadline;Prioridade;Percentual;Status\n");
    }

    @Override
    protected void escreverTarefa(Tarefa tarefa) throws Exception {
        String situacao = tarefa.getPercentual() >= 100.0 ? "CONCLUÍDA" : "PENDENTE";

        // Formato CSV com ponto e vírgula
        // Aspas duplas para evitar quebra se tiver ;
        buffer.append(String.format("\"%s\";\"%s\";%s;%d;%.1f;%s\n",
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getDeadline(),
                tarefa.getPrioridade(),
                tarefa.getPercentual(),
                situacao));
    }

    @Override
    protected void fecharArquivo() throws Exception {
        if (escritor != null) {
            escritor.write(buffer.toString());
            escritor.flush();
            escritor.close();
        }
    }
}
