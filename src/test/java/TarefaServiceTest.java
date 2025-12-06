import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import controle.services.TarefaService;
import interfaces.repositories.ITarefaRepository;
import interfaces.validators.IValidadorTarefa;
import modelo.Tarefa;
import modelo.Usuario; // Import necessário
import repositorios.TarefaCacheRepository; // Import necessário

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class TarefaServiceTest {

    private TarefaService tarefaService;
    private MockTarefaRepository mockRepository;
    private MockValidadorTarefa mockValidador;
    private TarefaCacheRepository mockCache; // Mock simples (ou real já que não usa rede no teste unitário simples)

    @BeforeEach
    void setUp() {
        mockRepository = new MockTarefaRepository();
        mockValidador = new MockValidadorTarefa();
        // Para testes simples, podemos passar null no cache se o service tratar, 
        // ou criar uma classe mock se necessário. Aqui usaremos null e ajustaremos o teste se quebrar,
        // ou idealmente instanciar um dummy.
        mockCache = new TarefaCacheRepository(); 
        
        tarefaService = new TarefaService(mockRepository, mockValidador, mockCache);
    }

    @Test
    void testCadastrarTarefaComSucesso() {
        // Arrange
        String titulo = "Tarefa Teste";
        String descricao = "Descrição teste";
        LocalDate deadline = LocalDate.now().plusDays(7);
        int prioridade = 3;
        Usuario usuario = new Usuario("Teste", "teste@teste.com", "123");

        // Act
        boolean resultado = tarefaService.cadastrar(titulo, descricao, deadline, prioridade, usuario);

        // Assert
        assertTrue(resultado);
        assertEquals(1, mockRepository.tarefas.size());
        assertEquals(titulo, mockRepository.tarefas.get(0).getTitulo());
    }

    @Test
    void testCadastrarTarefaComTituloInvalido() {
        // Arrange
        mockValidador.retornoValidacao = false;
        Usuario usuario = new Usuario("Teste", "teste@teste.com", "123");

        // Act
        boolean resultado = tarefaService.cadastrar("", "Descrição", LocalDate.now(), 1, usuario);

        // Assert
        assertFalse(resultado);
        assertEquals(0, mockRepository.tarefas.size());
    }

    // Mock classes atualizadas
    private static class MockTarefaRepository implements ITarefaRepository {
        List<Tarefa> tarefas = new ArrayList<>();

        @Override public void salvar(Tarefa tarefa) { tarefas.add(tarefa); }
        @Override public void atualizar(Tarefa antiga, Tarefa nova) {
            int index = tarefas.indexOf(antiga);
            if (index >= 0) tarefas.set(index, nova);
        }
        @Override public void remover(Tarefa tarefa) { tarefas.remove(tarefa); }
        @Override public Tarefa buscarPorTitulo(String titulo) { return null; }
        
        // --- Implementação dos novos métodos da interface ---
        @Override public List<Tarefa> listarPorUsuario(Usuario usuario) { return new ArrayList<>(tarefas); }
        @Override public List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario) { return new ArrayList<>(); }
        
        // Legados
        @Override public List<Tarefa> listarTodas() { return new ArrayList<>(tarefas); }
        @Override public List<Tarefa> listarPorData(LocalDate data) { return new ArrayList<>(); }
    }

    private static class MockValidadorTarefa implements IValidadorTarefa {
        boolean retornoValidacao = true;

        @Override public boolean validarTitulo(String titulo) { return retornoValidacao; }
        @Override public boolean validarDescricao(String descricao) { return retornoValidacao; }
        // Método obrigatório da interface atual:
        @Override public boolean validarTarefa(Tarefa tarefa) { return retornoValidacao; }
    }
}