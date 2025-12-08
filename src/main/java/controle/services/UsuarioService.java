package controle.services;

import interfaces.services.IUsuarioService;
import repositorios.UsuarioRepository;
import repositorios.TarefaRepository;
import repositorios.TarefaCacheRepository;
import modelo.Usuario;
import modelo.Tarefa;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.ArrayList;

public class UsuarioService implements IUsuarioService {
    private Usuario usuarioLogado;
    private UsuarioRepository usuarioRepository;
    private TarefaRepository tarefaRepository;
    private TarefaCacheRepository cacheRepository;

    public UsuarioService(UsuarioRepository uRepo, TarefaRepository tRepo) {
        this.usuarioRepository = uRepo;
        this.tarefaRepository = tRepo;
        this.cacheRepository = new TarefaCacheRepository(); 
    }

    public boolean cadastrar(String nome, String email, String senhaAberta) {
        if (usuarioRepository.buscarPorEmail(email) != null) return false;
        
        String senhaHash = BCrypt.hashpw(senhaAberta, BCrypt.gensalt());
        Usuario novo = new Usuario(nome, email, senhaHash);
        usuarioRepository.salvar(novo);
        return true;
    }

    @Override
    public boolean login(String email, String senha) {
        System.out.println("\n=== INICIANDO LOGIN: " + email + " ===");
        
        Usuario user = usuarioRepository.buscarPorEmail(email);

        if (user != null && user.getSenha() != null && BCrypt.checkpw(senha, user.getSenha())) {
            this.usuarioLogado = user;
            System.out.println("✅ Senha correta. Carregando tarefas...");

            long inicio = System.currentTimeMillis();
            return true;
        }
    
            /*
            
            // 1. Tenta Redis
            List<Tarefa> tarefas = cacheRepository.buscarCache(user.getId());
            
            if (tarefas == null) {
                // 2. Se falhar, busca SQL
                System.out.println("⚠️ Cache MISS - Buscando do SQL...");
                tarefas = tarefaRepository.listarPorUsuario(user);
                
                // 3. Salva no Redis se tiver dados
                if (tarefas != null && !tarefas.isEmpty()) {
                    cacheRepository.salvarCache(user.getId(), tarefas);
                }
            } else {
                System.out.println("✅ Cache HIT - Tarefas do Redis");
            }
            
            long fim = System.currentTimeMillis();
            System.out.println("⏱️ Tempo total de carregamento: " + (fim - inicio) + "ms");

            // 4. Injeta na memória
            user.setTarefas(tarefas != null ? tarefas : new ArrayList<>());
            
            return true;
        }
        
        System.out.println("❌ Falha no login.");
        return false;
        */
        return false;
    }

    // ... getters e setters padrão ...
    @Override public Usuario obterUsuario() { return usuarioLogado; }
    @Override public void alterarNome(String n) { if(usuarioLogado!=null) { usuarioLogado.setNome(n); usuarioRepository.salvar(usuarioLogado); }}
    @Override public String obterEmail() { return usuarioLogado != null ? usuarioLogado.getEmail() : null; }
    @Override public void definirSenha(String s) {} 
    @Override public boolean temSenha() { return true; }
    @Override public void logout() { usuarioLogado = null; }
    @Override public boolean isLogado() { return usuarioLogado != null; }
}