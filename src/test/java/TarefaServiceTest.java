import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import controle.services.TarefaService;
import interfaces.repositories.ITarefaRepository;
import interfaces.validators.IValidadorTarefa;
import modelo.Tarefa;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Testes unitários para TarefaService.
 * Demonstra como testar com injeção de dependência.
 */
public class TarefaServiceTest {
    
    private TarefaService tarefaService;
    private MockTarefaRepository mockRepository;
    private MockValidadorTarefa mockValidador;
    
    @BeforeEach
    void setUp() {
        mockRepository = new MockTarefaRepository();
        mockValidador = new MockValidadorTarefa();
        tarefaService = new TarefaService(mockRepository, mockValidador);
    }
    
    @Test
    void testCadastrarTarefaComSucesso() {
        // Arrange
        String titulo = "Tarefa Teste";
        String descricao = "Descrição teste";
        LocalDate deadline = LocalDate.now().plusDays(7);
        int prioridade = 3;
        
        // Act
        boolean resultado = tarefaService.cadastrar(titulo, descricao, deadline, prioridade);
        
        // Assert
        assertTrue(resultado);
        assertEquals(1, mockRepository.tarefas.size());
        assertEquals(titulo, mockRepository.tarefas.get(0).getTitulo());
    }
    
    @Test
    void testCadastrarTarefaComTituloInvalido() {
        // Arrange
        mockValidador.retornoValidacao = false;
        
        // Act
        boolean resultado = tarefaService.cadastrar("", "Descrição", LocalDate.now(), 1);
        
        // Assert
        assertFalse(resultado);
        assertEquals(0, mockRepository.tarefas.size());
    }
    
    @Test
    void testListarTodasTarefas() {
        // Arrange
        Tarefa tarefa1 = new Tarefa("Tarefa 1", "Desc 1", LocalDate.now(), LocalDate.now().plusDays(1), 1);
        Tarefa tarefa2 = new Tarefa("Tarefa 2", "Desc 2", LocalDate.now(), LocalDate.now().plusDays(2), 2);
        mockRepository.tarefas.add(tarefa1);
        mockRepository.tarefas.add(tarefa2);
        
        // Act
        List<Tarefa> resultado = tarefaService.listarTodas();
        
        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Tarefa 1", resultado.get(0).getTitulo());
        assertEquals("Tarefa 2", resultado.get(1).getTitulo());
    }
    
    // Mock classes para testes
    private static class MockTarefaRepository implements ITarefaRepository {
        List<Tarefa> tarefas = new ArrayList<>();
        
        @Override
        public void salvar(Tarefa tarefa) {
            tarefas.add(tarefa);
        }
        
        @Override
        public void atualizar(Tarefa antiga, Tarefa nova) {
            int index = tarefas.indexOf(antiga);
            if (index >= 0) {
                tarefas.set(index, nova);
            }
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
        public List<Tarefa> listarTodas() {
            return new ArrayList<>(tarefas);
        }
        
        @Override
        public List<Tarefa> listarPorData(LocalDate data) {
            return tarefas.stream()
                .filter(t -> t.getDeadline().equals(data))
                .collect(java.util.stream.Collectors.toList());
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
        public boolean validarDeadline(LocalDate deadline) {
            return retornoValidacao;
        }
        
        @Override
        public boolean validarPrioridade(int prioridade) {
            return retornoValidacao;
        }
    }
}