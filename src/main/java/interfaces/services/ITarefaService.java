package interfaces.services;

import java.time.LocalDate;
import java.util.List;

import modelo.Tarefa;
import modelo.Usuario;
import interfaces.observer.ISubject;

/**
 * Interface para serviços de gestão de tarefas.
 * <p>
 * Define operações CRUD para tarefas.
 * AGORA INTEGRALMENTE SEGURA: Métodos de listagem exigem Usuário.
 * </p>
 */
public interface ITarefaService extends ISubject {

    boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario);

    boolean editar(String tituloAntigo, String novoTitulo, String novaDescricao,
            LocalDate novoDeadline, int novaPrioridade, double novoPercentual);

    boolean excluir(String titulo);

    Tarefa buscarPorTitulo(String titulo);

    // --- MÉTODOS DE LEITURA (Agora exigem Usuario para segurança) ---

    List<Tarefa> listarPorUsuario(Usuario usuario);

    List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario);

    List<Tarefa> listarCriticasPorUsuario(Usuario usuario);

    // Strategy Pattern Seguro
    List<Tarefa> listar(interfaces.strategies.IFiltroStrategy filtro, Usuario usuario);

    List<Tarefa> listarOrdenado(interfaces.strategies.IOrdenacaoStrategy estrategia, Usuario usuario);

    void atualizarPercentual(Long idTarefa, double novoPercentual);
}