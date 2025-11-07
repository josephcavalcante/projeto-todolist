package negocio.services;

import java.time.LocalDate;
import java.util.List;

import interfaces.IEventoRepository;
import interfaces.IEventoService;
import interfaces.IValidadorEvento;
import modelo.Evento;

/**
 * Service responsável pela lógica de negócio dos eventos.
 * <p>
 * Centraliza operações CRUD de eventos, aplicando validações e
 * coordenando com o repositório. Segue o padrão Service Layer
 * e os princípios SRP e DIP.
 * </p>
 * 
 * @author Projeto ToDoList
 * @version 2.1
 * @since 2.1
 */
public class EventoService implements IEventoService {
    private IEventoRepository repositorio;
    private IValidadorEvento validador;
    
    /**
     * Construtor com injeção de dependência (DIP).
     * 
     * @param repositorio implementação do repositório de eventos
     * @param validador implementação do validador de eventos
     */
    public EventoService(IEventoRepository repositorio, IValidadorEvento validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }
    
    @Override
    public boolean cadastrar(String titulo, String descricao, LocalDate dataEvento, String local) {
        // Validação básica
        if (!validador.validarTitulo(titulo) || !validador.validarData(dataEvento) || 
            !validador.validarLocal(local)) {
            return false;
        }
        
        // Validação de conflito de eventos
        if (!validador.validarSemConflito(dataEvento, repositorio)) {
            return false; // Já existe evento nesta data
        }
        
        try {
            Evento novoEvento = new Evento(titulo.trim(), descricao.trim(), dataEvento, local.trim());
            
            // Validação do evento completo
            if (!validador.validarEvento(novoEvento)) {
                return false;
            }
            
            repositorio.salvar(novoEvento);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean editar(String tituloAntigo, LocalDate dataAntiga, String novoTitulo, 
                         String novaDescricao, LocalDate novaData, String novoLocal) {
        // Validação dos novos dados
        if (!validador.validarTitulo(novoTitulo) || !validador.validarData(novaData) || 
            !validador.validarLocal(novoLocal)) {
            return false;
        }
        
        try {
            // Busca o evento existente
            Evento eventoAntigo = repositorio.buscarPorTituloEData(tituloAntigo, dataAntiga);
            if (eventoAntigo == null) {
                return false;
            }
            
            // Verifica conflito apenas se a data mudou
            if (!dataAntiga.equals(novaData) && !validador.validarSemConflito(novaData, repositorio)) {
                return false;
            }
            
            // Cria novo evento com dados atualizados
            Evento eventoNovo = new Evento(novoTitulo.trim(), novaDescricao.trim(), novaData, novoLocal.trim());
            eventoNovo.setDataCadastro(eventoAntigo.getDataCadastro()); // Mantém data de cadastro original
            
            repositorio.atualizar(eventoAntigo, eventoNovo);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean remover(String titulo, LocalDate dataEvento) {
        try {
            Evento evento = repositorio.buscarPorTituloEData(titulo, dataEvento);
            if (evento != null) {
                repositorio.remover(evento);
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public List<Evento> listarTodosComDiasRestantes() {
        return repositorio.listarTodos(); // A entidade Evento já calcula dias restantes
    }
    
    @Override
    public List<Evento> listarPorData(LocalDate data) {
        return repositorio.listarPorData(data);
    }
    
    @Override
    public List<Evento> listarPorMes(int mes, int ano) {
        return repositorio.listarPorMes(mes, ano);
    }
    
    @Override
    public Evento buscarEvento(String titulo, LocalDate dataEvento) {
        return repositorio.buscarPorTituloEData(titulo, dataEvento);
    }
}