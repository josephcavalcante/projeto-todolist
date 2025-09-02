package validadores;

import interfaces.IValidadorTarefa;
import modelo.Tarefa;

// classe que so cuida das validacoes
// tirei essa logica do service pra ficar mais limpo
public class ValidadorTarefa implements IValidadorTarefa {
    
    @Override
    public boolean validarTitulo(String titulo) {
        return titulo != null && !titulo.trim().isEmpty();
    }
    
    @Override
    public boolean validarDescricao(String descricao) {
        return descricao != null;
    }
    
    @Override
    public boolean validarTarefa(Tarefa tarefa) {
        return tarefa != null && 
               validarTitulo(tarefa.getTitulo()) && 
               validarDescricao(tarefa.getDescricao()) &&
               tarefa.getDeadline() != null;
    }
}