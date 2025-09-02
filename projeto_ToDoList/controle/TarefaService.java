package controle;

import modelo.Tarefa;
import interfaces.IValidadorTarefa;
import interfaces.ITarefaRepository;
import validadores.ValidadorTarefa;
import repositorios.TarefaRepository;
import java.time.LocalDate;
import java.util.List;

// service refatorado pra usar as interfaces
// agora ele so coordena, nao faz validacao nem acesso direto aos dados
public class TarefaService {
    private ITarefaRepository repositorio;
    private IValidadorTarefa validador;

    public TarefaService(ManipuladorDeTarefas manipulador) {
        this.repositorio = new TarefaRepository(manipulador);
        this.validador = new ValidadorTarefa();
    }

    // criação de tarefa nova
    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade) {
        // validação usando validador dedicado
        if(!validador.validarTitulo(titulo)) {
            return false;
        }
        try {
            // instanciação da tarefa com data atual
            Tarefa novaTarefa = new Tarefa(titulo.trim(), descricao.trim(), LocalDate.now(), deadline, prioridade);
            repositorio.salvar(novaTarefa);
            return true; // operação bem-sucedida
        } catch (Exception ex) {
            return false; // falha na operação
        }
    }

    // edição de tarefa existente
    public boolean editar(String tituloAntigo, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade, double novoPercentual) {
        // verificação do novo título
        if (novoTitulo == null || novoTitulo.trim().length() == 0) {
            return false;
        }
        try {
            // localização da tarefa original
            Tarefa tarefaVelha = buscarPorTitulo(tituloAntigo);
            if (tarefaVelha == null) {
                return false; // tarefa inexistente
            }

            // construção da tarefa atualizada
            Tarefa tarefaEditada = new Tarefa(novoTitulo.trim(), novaDescricao.trim(), tarefaVelha.getDataCadastro(), novoDeadline, novaPrioridade);
            tarefaEditada.setPercentual(novoPercentual); // definição do percentual
            repositorio.atualizar(tarefaVelha, tarefaEditada);
            return true;
        } catch (Exception erro) {
            return false; // falha na edição
        }
    }

    // remoção de tarefa
    public boolean excluir(String titulo) {
        try {
            Tarefa tarefaParaRemover = buscarPorTitulo(titulo);
            if (tarefaParaRemover != null) {
                repositorio.remover(tarefaParaRemover);
                return true; // exclusão realizada
            }
            return false; // tarefa não localizada
        } catch (Exception ex) {
            return false; // falha na exclusão
        }
    }

    // busca de tarefa por título
    public Tarefa buscarPorTitulo(String titulo) {
        return repositorio.buscarPorTitulo(titulo);
    }
}