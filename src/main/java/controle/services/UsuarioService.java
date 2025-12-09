package controle.services;

import interfaces.services.IUsuarioService;
import repositorios.UsuarioRepository;
import repositorios.TarefaRepository;
import modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService implements IUsuarioService {
    private Usuario usuarioLogado;
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository uRepo, TarefaRepository tRepo) {
        this.usuarioRepository = uRepo;
    }

    public boolean cadastrar(String nome, String email, String senhaAberta) {
        if (usuarioRepository.buscarPorEmail(email) != null)
            return false;

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
            System.out.println("✅ Senha correta. Login verificado.");
            return true;
        }

        System.out.println("❌ Falha no login.");
        return false;
    }

    // ... getters e setters padrão ...
    @Override
    public Usuario obterUsuario() {
        return usuarioLogado;
    }

    @Override
    public void alterarNome(String n) {
        if (usuarioLogado != null) {
            usuarioLogado.setNome(n);
            usuarioRepository.salvar(usuarioLogado);
        }
    }

    @Override
    public String obterEmail() {
        return usuarioLogado != null ? usuarioLogado.getEmail() : null;
    }

    @Override
    public void definirSenha(String s) {
    }

    @Override
    public boolean temSenha() {
        return true;
    }

    @Override
    public void logout() {
        usuarioLogado = null;
    }

    @Override
    public boolean isLogado() {
        return usuarioLogado != null;
    }
}