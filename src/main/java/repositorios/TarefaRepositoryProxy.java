package repositorios;

import interfaces.repositories.ITarefaRepository;
import modelo.Tarefa;
import modelo.Usuario;
import java.time.LocalDate;
import java.util.List;

/**
 * Proxy de Cache para o Repositório de Tarefas.
 * Intercepta as chamadas para adicionar lógica de cache transparente.
 */
public class TarefaRepositoryProxy implements ITarefaRepository {

    private final ITarefaRepository repositorioReal; // O Banco SQL (TarefaRepository)
    private final TarefaCacheRepository cache;       // O Cache Redis

    // Injetamos as duas dependências via construtor
    public TarefaRepositoryProxy(ITarefaRepository real, TarefaCacheRepository cache) {
        this.repositorioReal = real;
        this.cache = cache;
    }

    @Override
    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        // 1. Tenta pegar do Cache (Rápido)
        List<Tarefa> tarefasCache = cache.buscarCache(usuario.getId());
        
        if (tarefasCache != null) {
            System.out.println("[PROXY] Cache HIT - Retornando do Redis.");
            return tarefasCache;
        }

        // 2. Se não achar, pega do Banco Real (Lento)
        System.out.println("[PROXY] Cache MISS - Buscando no SQL...");
        List<Tarefa> tarefasSQL = repositorioReal.listarPorUsuario(usuario);

        // 3. Salva no Cache para a próxima vez
        if (tarefasSQL != null) {
            cache.salvarCache(usuario.getId(), tarefasSQL);
        }

        return tarefasSQL;
    }

    @Override
    public void salvar(Tarefa tarefa) {
        // Salva no banco real
        repositorioReal.salvar(tarefa);
        // Invalida o cache do usuário, pois a lista mudou
        cache.invalidarCache(tarefa.getUsuario().getId());
    }

    @Override
    public void remover(Tarefa tarefa) {
        repositorioReal.remover(tarefa);
        cache.invalidarCache(tarefa.getUsuario().getId());
    }

    @Override
    public void atualizar(Tarefa antiga, Tarefa nova) {
        repositorioReal.atualizar(antiga, nova);
        cache.invalidarCache(antiga.getUsuario().getId());
    }

    // Métodos de leitura que talvez não usem cache (delegam direto)
    @Override
    public Tarefa buscarPorTitulo(String titulo) {
        return repositorioReal.buscarPorTitulo(titulo);
    }

    @Override
    public Tarefa buscarPorId(Long id) {
        return repositorioReal.buscarPorId(id);
    }

    @Override
    public List<Tarefa> listarTodas() {
        return repositorioReal.listarTodas();
    }
    
    // ... Implementar os outros métodos da interface apenas delegando para repositorioReal ...
    @Override public List<Tarefa> listarPorData(LocalDate data) { return repositorioReal.listarPorData(data); }
    @Override public List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario) { return repositorioReal.listarPorDataEUsuario(data, usuario); }
}