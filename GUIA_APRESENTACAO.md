# 🎓 Guia de Defesa - Padrões de Projeto (Sua Parte)

> **Dica de Ouro**: O professor não quer saber se você decorou o livro. Ele quer saber se você **entendeu o problema** que o padrão resolveu no **SEU** código.

---

## 1. Facade Pattern (Estrutural)

### 📖 O Conceito (GoF)
Fornecer uma interface unificada para um conjunto de interfaces em um subsistema. O Facade define uma interface de nível mais alto que torna o subsistema mais fácil de usar.

### 💻 No Nosso Projeto: `negocio.ToDoList`
*   **O que é:** É a classe principal que a Interface Gráfica (Swing) e o `Main` chamam.
*   **Como implementamos:**
    *   `ToDoList` não tem lógica "pesada". Ela apenas recebe o pedido da Tela e repassa para o Controller certo (`TarefaController`, `RelatorioController`).
    *   Ela esconde a complexidade de instanciar Services, Validadores e Repositórios.

### 🏆 Ganho (O Pulo do Gato)
*   **Antes:** A `Main` ou a `Tela` tinham que dar `new TarefaService(new Repo(new Conexao()))`. Se mudasse o banco, quebrava a tela.
*   **Depois:** A Tela só conhece `facade.adicionarTarefa()`. O Facade blinda a UI de mudanças no Backend.

### 💣 Pergunta do Professor (A Armadilha)
> *"Por que você usou Facade se você já tem Controllers? Não é redundante?"*
**Sua Resposta:** *"Não, professor. O Facade serve para **desacoplar** a UI de todo o ecossistema do backend. Se amanhã quisermos trocar o `TarefaController` por um microsserviço remoto, a Tela nem fica sabendo, pois ela só fala com o Facade. Além disso, o Facade organiza os métodos em um único ponto de entrada, facilitando o uso por qualquer frontend (Swing, Console ou Web)."*

### 🕵️‍♂️ Deep Dive: God Class & Singleton Implícito
**Pergunta:** *"O antigo ToDoList era uma God Class e um Singleton Implícito. Mas a gente ainda não cria só um ToDoList no Main?"*

1.  **Por que era God Class (Classe Deus)?**
    *   **Sintoma:** Ela sabia DEMAIS. Ela tinha a lista de tarefas (`ArrayList`), a salvar arquivo, login e filtros.
    *   **Problema:** Viertava o SRP. Mudar o arquivo quebrava o login.
    *   **Correção:** Agora ela é "burra". Só delega para `Controller` -> `Service`. Ela emagreceu.

2.  **Por que era Singleton Implícito?**
    *   **Antes (O Problema):** A lista de tarefas vivia *dentro* da classe em memória (`private List lista;`).
        *   Se você fizesse `new ToDoList()` na TelaA e `new ToDoList()` na TelaB, a TelaB **não veria** as tarefas da TelaA. Elas estariam isoladas.
        *   Por isso, éramos **obrigados** a passar a mesma instância `todolist` de tela em tela como se fosse um bastão em corrida de revezamento.
    *   **Agora (A Solução):** A classe não guarda dados (`Stateless`).
        *   Os dados estão no banco.
        *   Podemos até usar uma só instância por conveniência, mas se quiséssemos dar `new ToDoList()` em cada tela, **funcionaria perfeitamente**.
        *   Isso significa que removemos a **limitação arquitetural**. Não somos mais reféns da instância única.

---

## 2. Template Method (Comportamental)

### 📖 O Conceito (GoF)
Definir o esqueleto de um algoritmo em uma operação, postergando alguns passos para as subclasses. Permite que subclasses redefinam certos passos de um algoritmo sem mudar a estrutura do mesmo.

### 💻 No Nosso Projeto: `relatorios.RelatorioTemplate`
*   **O que é:** Classe abstrata que sabe a "receita de bolo" de gerar qualquer relatório.
*   **Como implementamos:**
    *   Método `gerar()` (final): Abre arquivo -> `gerarCabecalho()` -> `gerarCorpo()` -> `gerarRodape()` -> Fecha.
    *   Classes filhas (`RelatorioPDF`, `RelatorioCSV`) só implementam o recheio (`gerarCorpo`).

### 🏆 Ganho (O Pulo do Gato)
*   **Antes:** Tínhamos código duplicado ("Ctrl+C Ctrl+V") de abrir/fechar arquivo em toda classe de relatório.
*   **Depois:** A lógica de fluxo é única. Se precisarmos adicionar um "Log de Geração", alteramos só no pai e aplica para todos.

### 💣 Pergunta do Professor
> *"Qual a diferença entre Template Method e Strategy? Os dois não servem para trocar comportamentos?"*
**Sua Resposta:** *"Boa pergunta! A diferença é a estrutura. No **Strategy** (como fizemos nos Filtros), nós trocamos o algoritmo **inteiro** (composição). No **Template Method**, nós mantemos a estrutura do algoritmo fixa (herança) e trocamos apenas **etapas específicas**. Usamos Template no relatório porque a estrutura (abrir -> escrever -> fechar) é sempre igual, só muda a formatação."*

### 🔒 O Template Method (Relatórios)
**Pergunta:** *"Por que o método `gerarRelatorio` na classe `RelatorioTemplate` é `final`?"*
**Sua Resposta:** *"Essa é a essência do padrão **Template Method**.
O `final` serve para **proteger o algoritmo**. Nós definimos o esqueleto (Abrir -> Cabeçalho -> Corpo -> Rodapé -> Fechar) e proibimos que as subclasses mudem essa ordem.
As subclasses só podem preencher as lacunas (`hooks`), mas não podem quebrar a estrutura. É o princípio de Hollywood: 'Não nos chame, nós chamamos você'."*

### 🔒 Por que 'protected' nos métodos abstratos?
*   **Pergunta:** *"Por que `gerarCabecalho()` é `protected`? Por que não `private` ou `public`?"*
*   **Resposta:**
    *   **Não pode ser `public`:** Porque não queremos que ninguém chame `relatorio.escreverCabecalho()` fora de ordem.
    *   **Não pode ser `private`:** Porque senão a **classe filha** (`RelatorioPDF`) não conseguiria enxergar o método para implementá-lo (sobrescrever).
    *   **Solução (`protected`):** É o meio-termo perfeito. Visível para a filha (herança), mas invisível para o mundo externo (encapsulamento).

---

## 3. Singleton (Criacional)

### 📖 O Conceito (GoF)
Garantir que uma classe tenha somente uma instância e fornecer um ponto global de acesso para a mesma.

### 💻 No Nosso Projeto: `persistencia.DatabaseManager`
*   **O que é:** Gerenciador da conexão com o banco (EntityManagerFactory).
*   **Como implementamos:**
    *   Construtor `private`.
    *   Atributo estático `instance`.
    *   Método `getInstance()` que cria se não existir.

### 🏆 Ganho (O Pulo do Gato)
*   **Antes:** Cada `new TarefaRepository` abria uma conexão nova com o banco. Com 10 telas abertas, o banco travava (pool esgotado).
*   **Depois:** Temos apenas UMA conexão JPA compartilhada para a aplicação inteira. Economia brutal de memória e sockets.

### 💣 Pergunta do Professor
> *"O Singleton é considerado por muitos um 'Anti-Padrão' pois dificulta testes. Por que vocês usaram?"*
**Sua Resposta:** *"É verdade, ele pode ser perigoso se tiver estado mutável global (variáveis globais). Mas no nosso caso, usamos estritamente para **recursos de infraestrutura** (Conexão de Banco) que DEVEM ser únicos. Para mitigar o problema dos testes, nós encapsulamos o acesso dentro dos Repositórios, então podemos "mockar" o repositório sem tocar no Singleton."*

### 🧬 Anatomia do `DatabaseManager` (De cabo a rabo)
*   `private static DatabaseManager instance`: A variável estática que guarda a ÚNICA instância do mundo. Começa `null`.
*   `private DatabaseManager()`: **O Segredo**. O construtor é **privado**. Ninguém fora dessa classe pode dar `new DatabaseManager()`. Só ela mesma pode se criar.
*   `public static synchronized getInstance()`: O porteiro.
    *   **Lazy Loading**: Se `instance == null`, ele cria. Se já existe, ele só devolve. A instância só é criada quando alguém pede pela primeira vez (economiza memória na inicialização).
    *   **Synchronized**: Garante que se duas telas pedirem conexão ao mesmo tempo, não vai criar duas instâncias sem querer.
*   `EntityManagerFactory emf`: O recurso caro. Criar isso demora uns 2 segundos e abre 10 conexões. Se não fosse Singleton, cada clique do usuário travaria o sistema por 2 segundos.

---

## 4. Strategy (Comportamental)

### 📖 O Conceito (GoF)
Definir uma família de algoritmos, encapsular cada um, e torná-los intercambiáveis. Strategy permite que o algoritmo varie independentemente dos clientes que o utilizam.

### 💻 No Nosso Projeto: `strategies.IFiltroStrategy` e `IOrdenacaoStrategy`
*   **O que é:** Forma de filtrar e ordenar tarefas sem encher o Service de `if/else`.
*   **Como implementamos:**
    *   A interface define o contrato (`filtrar(lista)`).
    *   Implementações concretas: `FiltroPorData`, `FiltroCriticas`, `OrdenacaoPorPrioridade`.
    *   O `TarefaService` recebe a estratégia e executa, sem saber qual é.

### 🏆 Ganho (O Pulo do Gato)
*   **Antes:** O método `listar` tinha 50 linhas com checagens `if (tipo == "DATA")`. Para adionar um filtro novo, tinha que editar uma classe crítica.
*   **Depois:** (Open/Closed Principle) O Service está fechado para modificação. Quer um filtro novo "Por Nome"? Cria uma classe nova `FiltroPorNome`, passa pro Service e pronto. Zero risco de quebrar o que já funciona.

### 💣 Pergunta do Professor
> *"Você não poderia ter feito isso apenas com Polimorfismo simples (Herança)?"*
**Sua Resposta:** *"Com herança, eu ficaria preso a uma hierarquia rígida. Com **Strategy**, posso trocar a ordenação em tempo de execução."*

### 🕵️‍♂️ Deep Dive: Rigidez vs Flexibilidade (O Exemplo que Mata a Cobra)
*   **O que é "Rigidez da Herança"?**
    Se usássemos herança, teríamos que criar uma classe filha do Service para cada tipo de ordenação:
    *   `TarefaServiceOrdenadoPorData extends TarefaService`
    *   `TarefaServiceOrdenadoPorPrioridade extends TarefaService`
    *   **Problema:** Uma vez que eu dei `new TarefaServiceOrdenadoPorData()`, **não posso mudar**. O objeto nasceu assim e vai morrer assim. Se o usuário quiser mudar a ordenação no meio do uso, eu teria que matar o objeto e criar outro.

*   **O que é "Troca em Tempo de Execução" (Runtime)?**
    Com Strategy, o `TarefaService` é um só.
    *   Quando o usuário clica no ComboBox "Data", eu chamo: `service.listar(new PorDataStrategy())`.
    *   Um segundo depois, ele clica em "Prioridade", eu chamo: `service.listar(new PorPrioridadeStrategy())`.
    *   **Mágica:** Eu troquei o comportamento (o "cérebro" da ordenação) sem precisar recriar o Service. É como trocar o cartucho de um videogame: o console (Service) é o mesmo, o jogo (Strategy) muda.

### 🧠 A Complexidade do `listarOrdenado` (Onde brilha)
Aparentemente é simples, mas veja o fluxo complexo que ocorre em uma única linha:
`service.listarOrdenado(new OrdenacaoPorDataStrategy(), usuario)`

1.  **Repository (Proxy)**: Primeiro, o Service pede as dados brutos. O Proxy verifica no **Redis**. Se não achar, vai no **Postgres**. Retorna uma lista "crua".
2.  **Service + Strategy**: O Service pega essa lista crua e entrega para a estratégia (`estrategia.ordenar(lista)`).
3.  **Strategy**: A classe `OrdenacaoPorDataStrategy` usa Java Streams para reordenar a lista.
4.  **Resultado**: A lista volta para a tela, ordenada.

**A Sacada:** O Banco de Dados (SQL) **não sabe** que estamos ordenando. Nós desacoplamos a *Busca* (Infrastructure) da *Ordenação* (Business Logic). Se amanhã eu criar uma `OrdenacaoPorCor`, o Banco não precisa mudar nada.

### ⚖️ O Dilema Resolvido: Injeção de Dependência
**Pergunta:** *"Como vocês organizaram a criação das estratégias?"*
**Sua Resposta:** *"Adotamos o padrão de **Injeção de Dependência** via Facade.
O `ToDoList` (Facade) é quem dá o `new FiltroPorData()` ou `new OrdenacaoPorData()` e passa para o Service.
Isso deixa o `TarefaService` totalmente limpo e desacoplado: ele não conhece **nenhuma** classe concreta de estratégia, apenas as interfaces `IFiltroStrategy` e `IOrdenacaoStrategy`.
Isso facilita muito os testes unitários, pois podemos injetar mocks facilmente."*

---

## 5. Resumão: Padrões vs SOLID vs GRASP

Aqui está o mapa do tesouro para conectar os pontos na sua defesa:

| Padrão | SOLID (Princípio Atendido) | GRASP (Princípio Atendido) | Onde está no código? |
| :--- | :--- | :--- | :--- |
| **Facade** | **SRP** (Single Responsibility) | **Controlador**, **Acoplamento Baixo** | `negocio.ToDoList` |
| **Strategy** | **OCP** (Open/Closed) | **Polimorfismo** | `strategies.*` |
| **Template Method** | **DRY** (Don't Repeat Yourself), **SRP** | **Variações Protegidas** | `relatorios.RelatorioTemplate` |
| **Singleton** | - | **Especialista na Informação** (Infra) | `persistencia.DatabaseManager` |
| **Proxy** | **SRP** (Separação de Preocupações) | **Indireção** | `repositorios.TarefaRepositoryProxy` |
| **Factory** | **DIP** (Dependency Inversion) | **Fabricação Pura** | `factories.ServiceFactory` |
| **Observer** | **OCP** (Extensibilidade) | **Acoplamento Baixo** | `telas.TelaListarTarefas` |
| **Builder** | **SRP** (Construção vs Representação) | - | `builders.TarefaBuilder` |
