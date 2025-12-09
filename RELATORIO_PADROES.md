# 🧠 Relatório de Padrões de Projeto (Design Patterns)
> "A arquitetura é sobre as decisões importantes que são difíceis de mudar depois."

Este documento detalha os **7 Padrões de Projeto** implementados no ToDoList, comparando a abordagem ingênua (Antes) com a solução arquitetural (Depois).

---

## 1. Facade Pattern (Estrutural)
**Onde:** `negocio.ToDoList.java`

### ❌ Antes (God Class & Singleton Implícito)
A classe `ToDoList` era uma "God Class" clássica: ela fazia tudo (Carregava arquivo, gerenciava usuário, criava objetos, tratava exceções). Além disso, funcionava como um Singleton implícito, sendo instanciada na `Main` e passada por parâmetro para o sistema inteiro.
```java
// ToDoList.java (v1.0)
public class ToDoList {
    // Fazia tudo e conhecia todo mundo
    private Persistencia persistencia = new Persistencia(); 
    private ManipuladorDeTarefas manipulador = new ManipuladorDeTarefas();
    
    public void salvar() { ... } // Lógica de arquivo misturada com regra de negócio
}
```

### ✅ Depois (True Facade)
A classe `ToDoList` agora é uma **Fachada Real**. Ela não tem regra de negócio, nem estado global, nem persistência direta. Ela apenas **delega** e organiza o acesso aos subsistemas (Controllers/Services).
```java
// ToDoList.java (v2.0)
public class ToDoList {
    // Apenas delega. Não sabe como salvar, só sabe QUEM salva.
    public boolean adicionarTarefa(...) {
        return tarefaController.adicionar(...);
    }
}
```
**🏆 Ganho:** **Alta Coesão e Baixo Acoplamento**. A fachada esconde a complexidade (ex: não precisamos saber que existe um `RedisManager` ou `ServiceFactory`, basta chamar `toDoList.listar()`).

---

## 2. Observer Pattern (Comportamental)
**Onde:** `TelaListarTarefas.java` (Observer) e `TarefaService.java` (Subject)

### ❌ Antes (Refresh Manual)
O usuário tinha que fechar e abrir a tela ou clicar em "Atualizar" para ver mudanças feitas em outras janelas.
```java
// Usuario edita tarefa -> Tabela continua velha.
// Usuario precisa clicar no botão "Refresh".
```

### ✅ Depois (Reativo)
A tela "assina" atualizações do serviço.
```java
// TelaListarTarefas.java
sistema.getTarefaService().adicionarObservador(this);

// TarefaService.java
notificarObservadores("Tarefa Alterada"); // Acode todas as telas!
```
**🏆 Ganho:** **Reatividade**. A interface gráfica está sempre sincronizada com o estado real dos dados (Single Source of Truth).

---

## 3. Strategy Pattern (Comportamental)
**Onde:** `strategies.FiltroStrategy`

### ❌ Antes (Lógica Condicional Gigante)
Vários métodos ou `if/else` espalhados para filtrar tarefas.
```java
// TarefaService.java (Antigo)
if (tipo == "DATA") { ... }
else if (tipo == "CRITICAS") { ... }
else { return todas; }
```

### ✅ Depois (Polimorfismo)
Cada regra de filtro é uma classe. Podemos criar novos filtros sem tocar no código antigo.
```java
// TarefaService.java (Atual)
public List<Tarefa> listar(IFiltroStrategy filtro) {
    return filtro.filtrar(lista);
}
// Uso: listar(new FiltroPorDataStrategy(hoje));
```
**🏆 Ganho:** **Extensibilidade (Open/Closed Principle)**. Adicionar um filtro "Por Prioridade" não exige mudar 1 linha do Service.

---

## 4. Builder Pattern (Criacional)
**Onde:** `builders.TarefaBuilder`

### ❌ Antes (Construtor Telescópico)
Construtores confusos com muitos parâmetros nulos opcionais.
```java
new Tarefa("Titulo", null, "Descricao", null, 0, null...); // O que é o 4º null?
```

### ✅ Depois (Fluente)
Código legível que descreve o objeto sendo criado.
```java
new TarefaBuilder()
    .comTitulo("Estudar")
    .comPrazo(amanha)
    .comPrioridade(5)
    .construir();
```
**🏆 Ganho:** **Legibilidade e Flexibilidade**. Permite criar objetos complexos passo-a-passo.

---

## 5. Template Method (Comportamental)
**Onde:** `relatorios.RelatorioTemplate`

### ❌ Antes (Código Duplicado)
`RelatorioPDF` e `RelatorioCSV` tinham o mesmo código de "Abrir Arquivo -> Escrever Dados -> Fechar Arquivo". Só mudava a formatação.

### ✅ Depois (Esqueleto Compartilhado)
A classe mãe define o algoritmo, as filhas definem apenas o passo específico (`gerarCorpo()`).
```java
// RelatorioTemplate.java
public final void gerar() {
    abrirArquivo();
    gerarCabecalho(); // Abstrato
    gerarCorpo();     // Abstrato
    fecharArquivo();
}
```
**🏆 Ganho:** **Reuso de Código**. Evita duplicação da lógica de orquestração do relatório.

---

## 6. Factory Method (Criacional)
**Onde:** `factories.ServiceFactory`

### ❌ Antes (Instanciação Espalhada)
Cada lugar dava `new TarefaService(new Repo(new Connection()))`. Se o construtor mudasse, quebrava o projeto todo.

### ✅ Depois (Fábrica Central)
Lugar único responsável por montar as dependências.
```java
// ServiceFactory.java
public static ITarefaService criarTarefaService() {
    return new TarefaService(new TarefaRepository(), ...);
}
```
**🏆 Ganho:** **Inversão de Controle (IoC)**. Facilita testes (podemos injetar Mocks) e centraliza a configuração.

---

## 7. Singleton (Criacional)
**Onde:** `persistencia.DatabaseManager`

### ❌ Antes (Múltiplas Conexões)
Cada operação abria uma nova conexão com o Banco. Risco de estourar o limite de conexões.

### ✅ Depois (Instância Única)
Garante que a conexão com o JPA/Hibernate seja reaproveitada globalmente.
```java
public static DatabaseManager getInstance() { ... }
```
**🏆 Ganho:** **Performance e Gestão de Recursos**.
