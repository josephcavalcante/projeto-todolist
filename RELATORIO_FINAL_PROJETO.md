# 📄 Relatório Final de Arquitetura e Desenvolvimento: ToDoList 2.0

**Disciplina:** Padrões de Projeto
**Projeto:** Sistema de Gerenciamento de Tarefas (ToDoList)
**Data:** 10/12/2025
**Equipe:** Joseph Nichollas Abreu Cavalcante, Herbert Carvalho dos Santos

---

## 1. Introdução

Este relatório documenta a evolução arquitetural do projeto ToDoList, detalhando a transição de sua versão legada (v1.0) para a atual (v2.0). A análise baseia-se na comparação direta entre o código original (commit `fc7ce99`) e o estado atual, focando nas decisões de design iniciais, suas consequências negativas e as refatorações aplicadas para garantir escalabilidade e qualidade.

---

## 2. Decisões de Design "Questionáveis" (Análise do Legado v1.0)

Ao revisar o código-fonte da primeira versão entregue (`fc7ce99`), identificamos padrões que limitavam severamente a evolução do software.

### ❌ 2.1. A Serialização Monolítica (`controle.ManipuladorDeTarefas`)
*   **A Decisão:** A classe `ManipuladorDeTarefas` implementava `Serializable` e guardava todas as tarefas em uma `ArrayList` em memória. Para persistir, gravávamos o objeto inteiro.
*   **O Código Legado (v1.0):**
    ```java
    // controle/ManipuladorDeTarefas.java
    public class ManipuladorDeTarefas implements Serializable {
        private List<Tarefa> listaTarefas;

           public void cadastrarTarefa(String titulo, String descricao, String deadlineStr, int prioridade) throws Exception {
                try {
                    // conversão DD/MM/AAAA para LocalDate
                    String[] partesData = deadlineStr.split("/");
                    int dia = Integer.parseInt(partesData[0]);
                    int mes = Integer.parseInt(partesData[1]);
                    int ano = Integer.parseInt(partesData[2]);
                    
                    LocalDate prazoFinal = LocalDate.of(ano, mes, dia);
                    LocalDate dataAtual = LocalDate.now();
                    
                    Tarefa novaTarefa = new Tarefa(titulo, descricao, dataAtual, prazoFinal, prioridade);
                    adicionarTarefa(novaTarefa);
                } catch (Exception erro) {
                    throw new Exception("Data inválida. Use o formato DD/MM/AAAA");
                }
            }
        
        public List<Tarefa> listarTarefasCriticas() {
            LocalDate dataAtual = LocalDate.now();
            return listaTarefas.stream()
                .filter(tarefa -> tarefa.getDeadline().minusDays(tarefa.getPrioridade()).isBefore(dataAtual))
                .collect(Collectors.toList());
        }
    }
    ```
*   **Por que foi ruim:**
    *   **Monólito de Dados:** Se tivéssemos 1 milhão de tarefas, precisávamos carregar 1 milhão de objetos na RAM para editar um único título.
    *   **Violação de SRP:** A classe sabia gerenciar a lista, filtrar dados e até fazer parsing de String para Data (`DD/MM/AAAA`).
    *   **Fragilidade de Versão:** Como a classe implementava `Serializable`, qualquer mudança no código (ex: mudar nome de variável) impedia a leitura dos arquivos salvos anteriormente.

### ❌ 2.2. A "God Class" (`controle.ToDoList`) com Estado Global
*   **A Decisão:** A classe `ToDoList` acumulava responsabilidades de Controller, Facade e Persistência. Pior ainda, ela mantinha o estado da aplicação (`usuarioAtual` e `gerenciadorTarefas`) em memória.
*   **O Código Legado (v1.0):**
    ```java
    public class ToDoList {
        private ManipuladorDeTarefas gerenciadorTarefas;
        private Persistencia salvaDados;
        private Usuario usuarioAtual;
        private TarefaService serviceTarefas;
        private SubtarefaService serviceSubs;
        private static final String ARQUIVO_DADOS = "todolist.dat"; // arquivo onde salva

        // construtor - inicializa tudo
        public ToDoList() {
            this.gerenciadorTarefas = new ManipuladorDeTarefas();
            this.salvaDados = new Persistencia();
            this.usuarioAtual = new Usuario("Usuário", "projetopoo00@gmail.com"); // email fixo
            
            // tenta carregar dados salvos
            carregarDados();
            
            // cria os services
            this.serviceTarefas = new TarefaService(gerenciadorTarefas);
            this.serviceSubs = new SubtarefaService(gerenciadorTarefas, serviceTarefas);
        }

        // carrega os dados do arquivo
        private void carregarDados() {
            try {
                ManipuladorDeTarefas dadosCarregados = salvaDados.carregarManipulador(ARQUIVO_DADOS);
                if (dadosCarregados != null) {
                    this.gerenciadorTarefas = dadosCarregados; // substitui o vazio
                    // se tem usuario salvo, usa ele
                    if (dadosCarregados.getUsuario() != null) {
                        this.usuarioAtual = dadosCarregados.getUsuario();
                    }
                }
            } catch (Exception erro) {
                System.out.println("Arquivo de dados não encontrado. Iniciando com dados vazios.");
            }
    }
    ```
*   **Por que foi ruim:**
    *   **Acoplamento Temporal e de Sessão:** A instância de `ToDoList` "sabia" quem estava logado. Passar essa instância de uma tela para outra significava carregar todo o estado da sessão junto. Se o sistema reiniciasse, o login era perdido instantaneamente.
    *   **Acoplamento por Instanciação Concreta:** A classe violava o princípio de Inversão de Dependência (DIP) ao instanciar manualmente suas dependências com `new` (ex: `this.salvaDados = new Persistencia()`). Isso impedia a troca de implementações (como substituir a persistência em arquivo por banco de dados) sem reescrever a classe principal.

### ❌ 2.3. Persistência Binária Frágil (`persistencia.Persistencia`)
*   **A Decisão:** A classe `Persistencia` encapsulava a escrita direta de objetos Java em disco.
*   **O Código Legado (v1.0):**
    ```java
    public class Persistencia {

        // gravação do manipulador em arquivo
        public void salvarManipulador(ManipuladorDeTarefas manipulador, String nomeArquivo) {
            try (ObjectOutputStream gravador = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
                gravador.writeObject(manipulador);
            } catch (IOException erro) {
                erro.printStackTrace();
            }
        }

        // carregamento do manipulador do arquivo
        public ManipuladorDeTarefas carregarManipulador(String nomeArquivo) {
            try (ObjectInputStream leitor = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
                return (ManipuladorDeTarefas) leitor.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                return null; // arquivo inexistente ou corrompido
            }
        }
    } 
    ```
*   **Por que foi ruim:**
    *   **Fragilidade Extrema:** Qualquer alteração simples nas classes `Tarefa` ou `Usuario` (como adicionar um campo novo) alterava o `serialVersionUID`, causando `InvalidClassException`. Isso obrigava a limpar o banco de dados a cada deploy.
    *   **Escalabilidade Zero:** Ler e gravar o arquivo inteiro a cada `adicionarTarefa()` causava lentidão exponencial.
    
---

## 3. Refatorações e Bons Princípios (Clean Core & SOLID)

A versão 2.0 foi reescrita focando em desacoplamento e estabilidade.

### ✅ 3.1. Inversão de Controle e Persistência Poliglota
Abandonamos o arquivo binário e adotamos JPA (Hibernate).

*   **Refatoração:** Substituímos o método `salvarDados()` direto na `ToDoList` por Repositórios injetados.
*   **Impacto Positivo:**
    *   **Independência de Dados:** O `TarefaService` agora apenas chama `repositorio.salvar(tarefa)`. Ele não sabe se está salvando no Postgres ou no MongoDB (Subtarefas).
    *   **Segurança de Dados:** Alterar a classe `Tarefa` não corrompe o banco, pois o Hibernate gerencia a migração do Schema.

#### Código Antes (Legado):
```java
// ToDoList.java v1.0
public void removerTarefa(Tarefa tarefa) {
    gerenciadorTarefas.removerTarefa(tarefa);
    salvarDados(); // Gravação em arquivo forçada
}
```

#### Código Depois (v2.0):
```java
// TarefaService.java v2.0
public boolean excluir(String titulo, Usuario usuario) {
    Tarefa tarefa = buscarPorTitulo(titulo, usuario);
    if (tarefa != null) {
        repositorio.remover(tarefa); // Delega para o banco SQL
        return true;
    }
    return false;
}
```
### ✅ 3.2. Segregação de Validação (SRP)
Retiramos regras como "Título não pode ser vazio" de dentro do Controller/View e criamos classes especialistas.
*   **O que fizemos:** Criamos o pacote `validadores` (`ValidadorTarefa`, `ValidadorEvento`).
*   **Benefício:** Se a regra de validação mudar (ex: título agora precisa de 5 caracteres), alteramos apenas o `Validador`, sem risco de quebrar a persistência ou a tela.

### ✅ 3.3. Centralização da Criação (Simple Factory)
Utilizamos uma Factory para isolar a complexidade de instanciar objetos e suas dependências.
*   **Motivação:** Criar um `TarefaService` exige instanciar um `Validador`, um `Repository` SQL, um `Repository` Redis (Cache) e um `Proxy`. Espalhar essa lógica pelo sistema violaria DRY e aumentaria o acoplamento.
*   **O Código:**
    ```java
    public static ITarefaService criarTarefaService() {
        // 1. Cria os componentes crus
        ITarefaRepository sqlRepo = new TarefaRepository();
        TarefaCacheRepository redisRepo = new TarefaCacheRepository();

        // 2. Envolve o SQL com o Proxy de Cache
        ITarefaRepository repoComCache = new TarefaRepositoryProxy(sqlRepo, redisRepo);

        IValidadorTarefa validador = new ValidadorTarefa();

        // 3. Entrega o Proxy para o Service (O Service nem sabe que existe cache!)
        return new TarefaService(repoComCache, validador);
    }
    ```
### ✅ 3.4. Segregação de Interface (ISP)
Dividimos interfaces gigantes em menores e específicas.
*   **Exemplo:** Em vez de uma interface `ISistema` com 50 métodos, temos `ITarefaService`, `ISubtarefaService`, `IUsuarioService`.
*   **Ganho:** A classe `SubtarefaController` só precisa conhecer `ISubtarefaService`. Ela não tem acesso a métodos de Login ou Eventos, respeitando o princípio do menor privilégio.

---

## 4. Aplicação de Padrões de Projeto (GoF)

Design Patterns foram aplicados onde havia dor real no desenvolvimento.

### 🧩 4.1. Template Method (Relatórios)

*   **Motivação:** No commit `947d12e`, o `GeradorDeRelatorios` duplicava a lógica de abrir/fechar streams para PDF e CSV.
*   **Solução:** `RelatorioTemplate` define o fluxo `final gerarRelatorio()`.

#### Código Antes (1.0):
```java
public class GeradorDeRelatorios {
    
    // geração de relatório PDF diário
    public static void gerarRelatorioPDF(List<Tarefa> tarefas, LocalDate data) {
        Document documentoPDF = new Document();
        try {
            // configuração do arquivo PDF
            PdfWriter.getInstance(documentoPDF, new FileOutputStream("relatorio.pdf"));
            documentoPDF.open();
            documentoPDF.add(new Paragraph("Relatório de Tarefas do dia: " + data));
            documentoPDF.add(new Paragraph(" "));

            // verificação de existência de tarefas
            if (tarefas.isEmpty()) {
                documentoPDF.add(new Paragraph("Nenhuma tarefa encontrada para este dia."));
            } else {
                // iteração sobre as tarefas
                for (Tarefa item : tarefas) {
                    documentoPDF.add(new Paragraph("Título: " + item.getTitulo()));
                    documentoPDF.add(new Paragraph("Descrição: " + item.getDescricao()));
                    documentoPDF.add(new Paragraph("Deadline: " + item.getDeadline()));
                    documentoPDF.add(new Paragraph("Prioridade: " + item.getPrioridade()));
                    documentoPDF.add(new Paragraph("Percentual: " + item.getPercentual() + "%"));
                    documentoPDF.add(new Paragraph(" "));
                }
            }
        } catch (DocumentException | IOException erro) {
            erro.printStackTrace();
        } finally {
            documentoPDF.close();
        }
    }

    // criação de planilha Excel (formato CSV)
    public static void gerarRelatorioExcel(List<Tarefa> tarefas, int mes, int ano) {
        // implementação simples usando CSV
        try (FileOutputStream arquivoSaida = new FileOutputStream("relatorio_mensal.csv")) {
            StringBuilder conteudoCSV = new StringBuilder();
            conteudoCSV.append("Título,Descrição,Deadline,Prioridade,Percentual,Status\n");
            
            // processamento de cada tarefa
            for (Tarefa itemTarefa : tarefas) {
                String situacao = itemTarefa.getPercentual() >= 100.0 ? "CONCLUÍDA" : "PENDENTE"; // 100.0 explicito
                conteudoCSV.append(String.format("\"%s\",\"%s\",%s,%d,%.1f,%s\n",
                    itemTarefa.getTitulo(),
                    itemTarefa.getDescricao(),
                    itemTarefa.getDeadline(),
                    itemTarefa.getPrioridade(),
                    itemTarefa.getPercentual(),
                    situacao));
            }
            
            // gravação do conteúdo
            arquivoSaida.write(conteudoCSV.toString().getBytes("UTF-8"));
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }
} 
```

#### Código Depois (Padrão Aplicado):
##### Gerador De Relatorios
```java
public class GeradorDeRelatorios implements IRelatorioService {

    @Override
    public boolean gerarPDF(List<Tarefa> tarefas, LocalDate data) {
        RelatorioTemplate relatorio = new RelatorioPDF(data);
        return relatorio.gerarRelatorio(tarefas, "relatorio.pdf");
    }
```
##### Relatorio Template
```java
public abstract class RelatorioTemplate {
    public final boolean gerarRelatorio(List<Tarefa> tarefas, String caminhoArquivo) {
        try {
            abrirArquivo(caminhoArquivo);
            escreverCabecalho();
            if (tarefas.isEmpty()) {
                escreverCorpoVazio();
            } else {
                for (Tarefa tarefa : tarefas) {
                    escreverTarefa(tarefa);
                }
            }
            escreverRodape();
            fecharArquivo();
            System.out.println("Relatório gerado em: " + caminhoArquivo);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
            tratarErro(e);
            return false;
        }
    }

    // Hooks 
    protected abstract void abrirArquivo(String caminho) throws Exception;

    protected abstract void escreverCabecalho() throws Exception;

    protected abstract void escreverTarefa(Tarefa tarefa) throws Exception;

    protected abstract void fecharArquivo() throws Exception;
}
```
##### Relatorio PDF
```java
public class RelatorioPDF extends RelatorioTemplate {
    private Document documentoPDF;
    private LocalDate dataRelatorio;

    public RelatorioPDF(LocalDate data) {
        this.dataRelatorio = data;
    }

    protected void abrirArquivo(String caminho) throws Exception {
        documentoPDF = new Document();
        PdfWriter.getInstance(documentoPDF, new FileOutputStream(caminho));
        documentoPDF.open();
    }

    protected void escreverCabecalho() throws Exception {
        documentoPDF.add(new Paragraph("Relatório de Tarefas do dia: " + dataRelatorio));
        documentoPDF.add(new Paragraph(" ")); // Linha em branco
    }

    protected void escreverTarefa(Tarefa tarefa) throws Exception {
        documentoPDF.add(new Paragraph("Título: " + tarefa.getTitulo()));
        documentoPDF.add(new Paragraph("Descrição: " + tarefa.getDescricao()));
        documentoPDF.add(new Paragraph("Deadline: " + tarefa.getDeadline()));
        documentoPDF.add(new Paragraph("Prioridade: " + tarefa.getPrioridade()));
        documentoPDF.add(new Paragraph("Percentual: " + tarefa.getPercentual() + "%"));
        documentoPDF.add(new Paragraph("--------------------------------------------------"));
    }

    protected void fecharArquivo() throws Exception {
        if (documentoPDF != null) {
            documentoPDF.close();
        }
    }
}

```
---

### 🚦 4.2. Strategy (Estratégias de Filtro e Ordenação)

*   **Motivação:** O `ManipuladorDeTarefas` (v1.0) tinha métodos rígidos como `listarTarefasPorData` e `listarTarefasCriticas`. Se quiséssemos um novo filtro, teríamos que modificar a classe principal.
*   **Solução:** Interface `IFiltroStrategy`.

#### Código Antes (Legado):
```java
public class ManipuladorDeTarefas implements Serializable {
    // filtragem de tarefas por data específica
    public List<Tarefa> listarTarefasPorData(LocalDate data) {
        return listaTarefas.stream()
                .filter(tarefa -> tarefa.getDeadline().equals(data))
                .collect(Collectors.toList());
    }
}    
```

#### Código Depois (Strategy):

##### Interface Filtro :
```java
public interface IFiltroStrategy {
    List<Tarefa> filtrar(List<Tarefa> tarefas);
}
```
##### Filtro  por Data :
```java
public class FiltroPorDataStrategy implements IFiltroStrategy {
    private final LocalDate dataAlvo;

    public FiltroPorDataStrategy(LocalDate dataAlvo) {
        this.dataAlvo = dataAlvo;
    }

    public List<Tarefa> filtrar(List<Tarefa> tarefas) {
        if (dataAlvo == null) {
            return tarefas; // Se data é null, retorna tudo (ou podia retornar vazio)
        }
        return tarefas.stream()
                .filter(t -> t.getDeadline().equals(dataAlvo))
                .collect(Collectors.toList());
    }
}
```
##### Facade :
```java
public class ToDoList {
    public List<Tarefa> listarTarefasPorData(LocalDate data) {
        Usuario usuario = usuarioController.obterUsuario();
        if (usuario != null) {
            return tarefaController.listar(new FiltroPorDataStrategy(data), usuario);
        }
        return Collections.emptyList();
    }
}
```
##### Tarefa Service :
```java
public class TarefaService implements ITarefaService {
    public List<Tarefa> listar(interfaces.strategies.IFiltroStrategy filtro, Usuario usuario) {
        if (usuario == null) return new ArrayList<>();

        List<Tarefa> todasTarefas = repositorio.listarPorUsuario(usuario);

        if (todasTarefas == null) {
            todasTarefas = new ArrayList<>();
        }

        // Aplica o filtro (Strategy) em memória sobre os dados retornados
        return filtro.filtrar(todasTarefas);
    }
}
```
*   **Consequência:** Adicionar um filtro "Por Nome" agora envolve apenas criar uma nova classe, sem risco de quebrar os filtros existentes (OCP).

---

### 🏯 4.3. Facade (`ToDoList` Refatorado)
*   **Problema:** A classe `ToDoList`era uma "God Class" clássica: ela fazia tudo (Carregava arquivo, gerenciava usuário, criava objetos, tratava exceções).
*   **Solução:** A classe `ToDoList` agora é uma Fachada Real. Ela não tem regra de negócio, nem estado global, nem persistência direta. Ela apenas delega e organiza o acesso aos subsistemas.

#### v1.0 (God Class) :
```java
    public class ToDoList {
        private ManipuladorDeTarefas gerenciadorTarefas;
        private Persistencia salvaDados;
        private Usuario usuarioAtual;
        private TarefaService serviceTarefas;
        private SubtarefaService serviceSubs;
        private static final String ARQUIVO_DADOS = "todolist.dat"; // arquivo onde salva

        // construtor - inicializa tudo
        public ToDoList() {
            this.gerenciadorTarefas = new ManipuladorDeTarefas();
            this.salvaDados = new Persistencia();
            this.usuarioAtual = new Usuario("Usuário", "projetopoo00@gmail.com"); // email fixo
            
            // tenta carregar dados salvos
            carregarDados();
            
            // cria os services
            this.serviceTarefas = new TarefaService(gerenciadorTarefas);
            this.serviceSubs = new SubtarefaService(gerenciadorTarefas, serviceTarefas);
        }

        // carrega os dados do arquivo
        private void carregarDados() {
            try {
                ManipuladorDeTarefas dadosCarregados = salvaDados.carregarManipulador(ARQUIVO_DADOS);
                if (dadosCarregados != null) {
                    this.gerenciadorTarefas = dadosCarregados; // substitui o vazio
                    // se tem usuario salvo, usa ele
                    if (dadosCarregados.getUsuario() != null) {
                        this.usuarioAtual = dadosCarregados.getUsuario();
                    }
                }
            } catch (Exception erro) {
                System.out.println("Arquivo de dados não encontrado. Iniciando com dados vazios.");
            }
    }
 ```

#### v2.0 Facade : 
Não tem estado. Apenas encaminha pro `controller`
```java
public class ToDoList {
    public boolean login(String email, String senha) {
        return usuarioController.login(email, senha);
    }

    public boolean cadastrarUsuario(String nome, String email, String senha) {
        return usuarioController.cadastrar(nome, email, senha);
    }

    public void logout() {
        usuarioController.logout();
    }

    public boolean isUsuarioLogado() {
        return usuarioController.isLogado();
    }

    public Usuario obterUsuario() {
        return usuarioController.obterUsuario();
    }
 ```
---


### 📡 4.4. Observer (Notificações)

*   **Problema:** Para beneficiar a escalabilidade, tarefa como ponto mais importante do app, precisava de uma maneira padronizada e fácil de avisar aos interessados quando uma tarefa é alterada.
*   **Solução:** O `TarefaService` notifica observadores registrados.
#### Código:
##### Interface Observer:
 ```java
public interface IObserver {
    void atualizar(Object mensagem);
}
```
##### Interface Subject:
 ```java
public interface ISubject {
    void adicionarObservador(IObserver observer);
    void removerObservador(IObserver observer);
    void notificarObservadores(Object mensagem);
}
```
##### Subject Concreto (Tarefa Serice):
 ```java
public class TarefaService implements ITarefaService, ISubject {
    private List<interfaces.observer.IObserver> observadores = new ArrayList<>();

    public void adicionarObservador(interfaces.observer.IObserver observer) {
        observadores.add(observer);
    }
    public void removerObservador(interfaces.observer.IObserver observer) {
        observadores.remove(observer);
    }
    public void notificarObservadores(Object mensagem) {
        for (interfaces.observer.IObserver observer : observadores) {
            observer.atualizar(mensagem);
        }
    }
    public void atualizarPercentual(Long idTarefa, double novoPercentual) {
        try {
            Tarefa tarefa = repositorio.buscarPorId(idTarefa);
            if (tarefa != null) {
                tarefa.setPercentual(novoPercentual);
                repositorio.salvar(tarefa);
                notificarObservadores("Tarefa atualizada: " + tarefa.getTitulo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
```
#####  Observer Concreto (TelaListarTarefas):
 ```java
public class TelaListarTarefas extends JPanel implements IObserver {
    public void removeNotify() {
        sistema.getTarefaService().removerObservador(this);
        super.removeNotify();
    }
    public void atualizar(Object mensagem) {
        SwingUtilities.invokeLater(() -> {
            carregarTarefas(this.dataFiltro);
        });
    }
```
---
### 🛡️ 4.5. Proxy (Cache Transparente)

*   **Problema:** Adicionar cache no Service sujava a regra de negócio com lógica de infraestrutura (Redis).
*   **Solução:** O `TarefaRepositoryProxy` finge ser um repositório comum, mas gerencia o cache, pois envolve o repositório real e intercepta as chamadas. Service nem sabe que existe cache, ele acredita que está falando com o banco de dados.
#### Código antigo:
```java
public class TarefaService implements ITarefaService {
    private ITarefaRepository repositorio;
    private IValidadorTarefa validador;
    private TarefaCacheRepository cacheRepository;

    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario) {
        if (!validador.validarTitulo(titulo)) return false;
        try {
            Tarefa novaTarefa = new Tarefa(titulo.trim(), descricao.trim(), LocalDate.now(), deadline, prioridade);
            novaTarefa.setUsuario(usuario);
            
            // 1. Persistência Real (SQL)
            repositorio.salvar(novaTarefa);
            
            // 2. Atualiza Memória RAM (para a tela ver imediatamente)
            usuario.getTarefas().add(novaTarefa);
            
            // 3. Invalida Redis (para forçar recarregamento no próximo login)
            cacheRepository.invalidarCache(usuario.getEmail());
            System.out.println("[SYNC] Tarefa criada. Redis invalidado e memória atualizada.");
            
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
```
#### Código novo:
##### Service Factory:
```java
public class ServiceFactory {
    public static ITarefaService criarTarefaService() {
        // 1. Cria os componentes crus
        ITarefaRepository sqlRepo = new TarefaRepository();
        TarefaCacheRepository redisRepo = new TarefaCacheRepository();

        // 2. Envolve o SQL com o Proxy de Cache
        ITarefaRepository repoComCache = new TarefaRepositoryProxy(sqlRepo, redisRepo);

        IValidadorTarefa validador = new ValidadorTarefa();

        // 3. Entrega o Proxy para o Service (O Service nem sabe que existe cache!)
        return new TarefaService(repoComCache, validador);
```
##### Service Factory:
```java
public class TarefaRepositoryProxy implements ITarefaRepository {

    private final ITarefaRepository repositorioReal; // O Banco SQL (TarefaRepository)
    private final TarefaCacheRepository cache;       // O Cache Redis

    // Injetamos as duas dependências via construtor
    public TarefaRepositoryProxy(ITarefaRepository real, TarefaCacheRepository cache) {
        this.repositorioReal = real;
        this.cache = cache;
    }

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
    public void salvar(Tarefa tarefa) {
        // Salva no banco real
        repositorioReal.salvar(tarefa);
        // Invalida o cache do usuário, pois a lista mudou
        cache.invalidarCache(tarefa.getUsuario().getId());
    }
    public void remover(Tarefa tarefa) {
        repositorioReal.remover(tarefa);
        cache.invalidarCache(tarefa.getUsuario().getId());
    }
    public void atualizar(Tarefa antiga, Tarefa nova) {
        repositorioReal.atualizar(antiga, nova);
        cache.invalidarCache(antiga.getUsuario().getId());
    }
}
```    
##### Tarefa Service:
```java
public class TarefaService implements ITarefaService, ISubject {

    // O Repositório aqui será, em tempo de execução, o TarefaRepositoryProxy
    private ITarefaRepository repositorio;
    private IValidadorTarefa validador;

    public boolean excluir(String titulo, Usuario usuario) {
        try {
            // Busca segura
            Tarefa tarefa = buscarPorTitulo(titulo, usuario);
            if (tarefa != null) {
                // O Proxy intercepta, remove do SQL e invalida o cache
                repositorio.remover(tarefa);
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }    
}
```
---

### 🏗️ 4.6. Builder (Construção Fluente)

*   **Problema:** A entidade `Tarefa` tinha muitos atributos opcionais, levando a construtores confusos com muitos parâmetros nulos opcionais.
*   **Solução:** `TarefaBuilder` permite criação passo-a-passo, além de construções personalizadas.
#### Código antigo :
```java
public class TarefaService {
    // criação de tarefa nova
    public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade) {
        // validação da existência do título
        if(titulo == null || titulo.trim().equals("")) { // sem espaço no if
            return false; // título obrigatório
        }
        try {
            // instanciação da tarefa com data atual
            Tarefa novaTarefa = new Tarefa(titulo.trim(), descricao.trim(), LocalDate.now(), deadline, prioridade);
        ...}
    }
}       
```
#### Código novo:
##### Builder:
```java
public class TarefaBuilder {
    private String titulo;
    private String descricao;
    private LocalDate dataCadastro;
    private LocalDate deadline;
    private int prioridade;
    private double percentual;

    public TarefaBuilder() {
        // Valores padrão
        this.dataCadastro = LocalDate.now();
        this.percentual = 0.0;
        this.prioridade = 1; // Prioridade mínima padrão
        this.descricao = "";
    }

    public TarefaBuilder comTitulo(String titulo) {
        this.titulo = titulo != null ? titulo.trim() : null;
        return this;
    }

    public TarefaBuilder comDescricao(String descricao) {
        this.descricao = descricao != null ? descricao.trim() : "";
        return this;
    }
    ...

    public Tarefa construir() {
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalStateException("Título da tarefa é obrigatório.");
        }
        if (deadline == null) {
            throw new IllegalStateException("Data limite (Deadline) é obrigatória.");
        }
        Tarefa tarefa = new Tarefa(titulo, descricao, dataCadastro, deadline, prioridade);
        tarefa.setPercentual(percentual);
        return tarefa;
    }
}
```
##### Tarefa Service:
```java
public class TarefaService implements ITarefaService, ISubject {
   public boolean cadastrar(String titulo, String descricao, LocalDate deadline, int prioridade, Usuario usuario) {
        // 1. Validação preliminar
        if (!validador.validarTitulo(titulo)) {
            return false;
        }
        try {
            // 2. Construção do Objeto
            Tarefa novaTarefa = new TarefaBuilder()
                    .comTitulo(titulo)
                    .comDescricao(descricao)
                    .comPrazo(deadline)
                    .comPrioridade(prioridade)
                    .construir();
             ...
        }
   }
}            
```
### 👑 4.7. Singleton (Instância Única)

*   **Problema:** Criar múltiplas instâncias da `EntityManagerFactory` do Hibernate consome muita memória e pools de conexão.
*   **Solução:** O `DatabaseManager` garante uma única instância global sendo reaproveitada durante todo o ciclo de vida do app.
*   **Código:**
```java
public class DatabaseManager {
    private static DatabaseManager instance;
    private EntityManagerFactory emf;

    private DatabaseManager() {
        try {
            this.emf = Persistence.createEntityManagerFactory("todoListPU");
        } catch (Exception e) {
            System.err.println("FATAL: Erro ao conectar no Banco SQL (Porta 5433, 5432).");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        if (emf != null)
            emf.close();
    }
}
```

---
## 5. Conclusão 
### 5.1. A Curva de Aprendizado
O projeto foi uma jornada de transformação no nosso entendimento sobre desenvolvimento de software.

A análise do código legado (`fc7ce99`) revela como decisões iniciais e amadoras focadas em "fazer funcionar" criaram um grande débito. A persistência binária, por exemplo, impedia mudanças no modelo, a classe `ManipuladorDeTarefas` e `ToDoList` centralizava regras de negócio, persistência e até parsing de strings, tornando impossível a escalabilidade/extensibilidade e testes isolados.

A arquitetura final v2.0, com a quebra dessas classes monolíticas em Services e Repositories, transformou o projeto em uma base sólida, testável e pronta para escalar.

### 5.2. O Desafio da Refatoração
Refatorar o sistema foi consideravelmente mais difícil do que escrevê-lo do zero. Desacoplar a persistência binária para injetar o JPA foi um processo doloroso, pois a lógica de UI estava misturada com regras de negócio. Tivemos que "trocar o motor com o carro andando". A maior lição foi: **código mal projetado cobra juros altos**. Cada atalho que foi tomado na v1.0 nos custou horas de depuração na v2.0.

### 5.3. O Resultado
Hoje, temos orgulho do código que produzimos. O sistema não apenas funciona, mas é elegante. A aplicação de padrões não foram apenas para cumprir requisistos, mas resolveu problemas reais que o código tinha, além de permitir a extensibilidade.

O aprendizado e o uso de estruturas na prática como `Simple Factory`, `Repository` , `Controller` e `Services` podem ser replicadas em qualquer aplicação futura, acredito que demos um passo importante na carreira de programadores, nos tornamos mais capazes de criar sistemas que sobrevivam ao tempo.
