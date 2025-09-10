package factories;

import controle.ManipuladorDeTarefas;
import controle.TarefaService;
import interfaces.ITarefaRepository;
import interfaces.IValidadorTarefa;
import interfaces.IRelatorioService;
import repositorios.TarefaRepository;
import validadores.ValidadorTarefa;
import servicos.RelatorioService;

// factory pra criar os services - GRASP Creator + OCP
// facilita criacao e permite trocar implementacoes
public class ServiceFactory {
    
    public static TarefaService criarTarefaService(ManipuladorDeTarefas manipulador) {
        ITarefaRepository repositorio = new TarefaRepository(manipulador);
        IValidadorTarefa validador = new ValidadorTarefa();
        return new TarefaService(repositorio, validador);
    }
    
    public static IRelatorioService criarRelatorioService() {
        return new RelatorioService();
    }
    
    // metodo pra trocar implementacao do validador facilmente
    public static TarefaService criarTarefaServiceComValidador(ManipuladorDeTarefas manipulador, IValidadorTarefa validador) {
        ITarefaRepository repositorio = new TarefaRepository(manipulador);
        return new TarefaService(repositorio, validador);
    }
}