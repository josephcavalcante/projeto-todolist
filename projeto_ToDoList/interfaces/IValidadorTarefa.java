package interfaces;

import modelo.Tarefa;

// interface pra validar tarefas
// coloquei as validacoes numa classe separada
public interface IValidadorTarefa {
    boolean validarTitulo(String titulo);
    boolean validarDescricao(String descricao);
    boolean validarTarefa(Tarefa tarefa);
}