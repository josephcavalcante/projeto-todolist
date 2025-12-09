import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import controle.services.TarefaService;
import interfaces.repositories.ITarefaRepository;
import interfaces.validators.IValidadorTarefa;
import modelo.Tarefa;
import modelo.Usuario;
import repositorios.TarefaCacheRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Testes unitários para TarefaService.
 * Demonstra como testar com injeção de dependência.
 */
public class TarefaServiceTest {

    private TarefaService tarefaService;
    private MockTarefaRepository mockRepository;
    private MockValidadorTarefa mockValidador;
    private TarefaCacheRepository mockCache;

    @BeforeEach
    void setUp() {
        mockRepository = new MockTarefaRepository();
        mockValidador = new MockValidadorTarefa();
        mockCache = new MockCache();

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

    @Test
    void testListarTodasTarefasPorUsuario() {
        // Arrange
        Usuario usuario = new Usuario("Teste", "teste@teste.com", "123");

        Tarefa tarefa1 = new builders.TarefaBuilder()
                .comTitulo("Tarefa 1")
                .comDescricao("Desc 1")
                .comPrazo(LocalDate.now().plusDays(1))
                .comPrioridade(1)
                .construir();
        Tarefa tarefa2 = new builders.TarefaBuilder()
                .comTitulo("Tarefa 2")
                .comDescricao("Desc 2")
                .comPrazo(LocalDate.now().plusDays(2))
                .comPrioridade(2)
                .construir();

        // Simula que o repositório retorna tarefas para esse usuário
        mockRepository.tarefas.add(tarefa1);
        mockRepository.tarefas.add(tarefa2);

        // Act
        List<Tarefa> resultado = tarefaService.listarPorUsuario(usuario);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Tarefa 1", resultado.get(0).getTitulo());
        assertEquals("Tarefa 2", resultado.get(1).getTitulo());
    }

    // Mocks atualizados
    private static class MockTarefaRepository implements ITarefaRepository {
        List<Tarefa> tarefas = new ArrayList<>();

        @Override
        public void salvar(Tarefa tarefa) {
            tarefas.add(tarefa);
        }

        @Override
        public void atualizar(Tarefa antiga, Tarefa nova) {
            int index = tarefas.indexOf(antiga);
            if (index >= 0)
                tarefas.set(index, nova);
        }

        @Override
        public void remover(Tarefa tarefa) {
            tarefas.remove(tarefa);
        }

        @Override
        public Tarefa buscarPorTitulo(String titulo) {
            return tarefas.stream()
                    .filter(t -> t.getTitulo().equals(titulo))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public Tarefa buscarPorId(Long id) {
            return tarefas.stream()
                    .filter(t -> t.getId() != null && t.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Tarefa> listarTodas() {
            return new ArrayList<>(tarefas);
        }

        @Override
        public List<Tarefa> listarPorUsuario(Usuario usuario) {
            return new ArrayList<>(tarefas);
        }

        @Override
        public List<Tarefa> listarPorDataEUsuario(LocalDate data, Usuario usuario) {
            return new ArrayList<>();
        }

        @Override
        public List<Tarefa> listarPorData(LocalDate data) {
            return new ArrayList<>();
        }
    }

    private static class MockValidadorTarefa implements IValidadorTarefa {
        boolean retornoValidacao = true;

        @Override
        public boolean validarTitulo(String titulo) {
            return retornoValidacao;
        }

        @Override
        public boolean validarDescricao(String descricao) {
            return retornoValidacao;
        }

        @Override
        public boolean validarTarefa(Tarefa tarefa) {
            return retornoValidacao;
        }
    }

    @Test
    void testListarComUsuarioGaranteSeguranca() {
        // Arrange
        // (usuario null)

        // Act
        // listar com null deve retornar vazio devido a trava de segurança
        List<Tarefa> resultado = tarefaService.listar(t -> t, null);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    private static class MockCache extends TarefaCacheRepository {
        @Override
        public void salvarCache(Long id, List<Tarefa> t) {
        }

        @Override
        public List<Tarefa> buscarCache(Long id) {
            return null;
        }

        @Override
        public void invalidarCache(Long id) {
        }
    }
}