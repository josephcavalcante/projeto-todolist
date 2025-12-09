# 🆚 Comparativo de Evolução: v1.0 vs v2.0

Este documento detalha a transformação técnica do projeto **ToDoList**, comparando a versão original (`tag: v1.0`) com a versão atual (`HEAD`).

## 📊 Resumo Executivo
| Critério | Versão 1.0 (Legado) | Versão 2.0 (Atual) | Mudança |
| :--- | :--- | :--- | :--- |
| **Persistência** | Arquivo binário (`todolist.dat`) via `Persistencia.java` | **PostgreSQL** (SQL), **MongoDB** (NoSQL) e **Redis** (Cache) | 🔥 Crítica |
| **Build System** | Scripts manuais (`.bat`) | **Gradle** (Automação, Dependências, Testes) | 🚀 Alta |
| **Padrões de Projeto** | Singleton (Ingênuo) | **Facade, Strategy, Observer, Builder, DAO, Template Method** | 🧠 Arquitetural |
| **Interface (UI)** | GridBagLayout (Complexo), Lógica misturada na tela | **BorderLayout**, FlatLaf (Moderno), MVC estrito | 🎨 Visual/Técnica |
| **Segurança** | Sem Hash, Persistência insegura | **BCrypt** (Hash de Senha), Validação de Sessão | 🔒 Alta |
| **Testes** | Inexistentes | **JUnit 5** + Mocking | ✅ Qualidade |

---

## 🛠️ Análise Técnica Detalhada

### 1. Camada de Persistência
*   **Antes (v1.0)**: O sistema utilizava a classe `persistencia.Persistencia` que serializava o objeto `ManipuladorDeTarefas` inteiro para um arquivo binário (`todolist.dat`) via `ObjectOutputStream`.
    *   *Problema Crítico*: Se a classe `Tarefa` mudasse um atributo, a desserialização falhava (`InvalidClassException`). Toda a base de dados era perdida.
    *   *Escalabilidade Nula*: Impossível dois usuários escreverem ao mesmo tempo (Lock de arquivo).
*   **Depois (v2.0)**: Implementação **Polyglot Persistence**.
    *   **PostgreSQL**: Dados estruturados (Usuários, Tarefas).
    *   **MongoDB**: Dados flexíveis (Subtarefas).
    *   **Redis**: Cache de leitura (Velocidade instantânea).
    *   *Ganho*: O sistema agora suporta milhares de usuários concorrentes e schemas evolutivos.

### 2. Estrutura do Código
*   **Antes (v1.0)**:
    *   **God Class**: A classe `ToDoList` centralizava tudo: tinha referências para `Persistencia`, `ManipuladorDeTarefas`, `Usuario`, `Services`.
    *   **Singleton Implícito**: `ToDoList` era instanciada na `Main` e repassada via construtor para todas as telas (Dependency Drilling).
    *   **Acoplamento**: `TarefaService` recebia `ManipuladorDeTarefas` direto no construtor.
*   **Depois (v2.0)**: Adoção estrita de **SOLID**.
    *   `Interfaces`: Todo Service/Repository tem uma Interface (`ITarefaService`).
    *   `Facade`: A classe `ToDoList` virou uma fachada que apenas delega (Blindagem/Encapsulamento).
    *   `Strategy`: Filtros de tarefas (`Criticas`, `PorData`) são classes separadas, não `stream()` filters hardcoded no `Manipulador`.

### 3. Interface Gráfica e UX
*   **Antes (v1.0)**: `TelaLogin` usava `GridBagLayout` com mais de 100 linhas apenas para posicionar 2 campos. A tela travava durante operações pesadas.
*   **Depois (v2.0)**:
    *   Refatoração para `BorderLayout` (Código limpo).
    *   Implementação do **Observer Pattern**: A `TelaListarTarefas` se atualiza sozinha quando o backend muda.
    *   Uso de `FlatLaf` (sugerido) para visual profissional.

### 4. Segurança
*   **Antes (v1.0)**: Senhas possivelmente salvas em texto puro ou serializadas.
*   **Depois (v2.0)**: Uso da biblioteca `BCrypt`. Mesmo se o banco for vazado, as senhas estão ilegíveis. O Redis usa chaves seguras (`tarefas:uid:1` ao invés de email).

### 5. Compilação e Deploy
*   **Antes (v1.0)**: Dependia de `.bat` e da IDE do usuário. Bibliotecas (`jar`) jogadas na pasta.
*   **Depois (v2.0)**: **Gradle**.
    *   `./gradlew build`: Baixa dependências (JPA, Driver Postgres, Jedis) automaticamente.
    *   `docker-compose up`: Sobe toda a infraestrutura (Bancos + Cache) com um comando.

## 🏆 Conclusão
O projeto deixou de ser um "Trabalho Acadêmico Simples" para se tornar uma **Aplicação Enterprise**. A arquitetura atual permite:
1.  **Escalar** (adicionar mais servidores).
2.  **Manter** (adicionar features sem quebrar o resto).
3.  **Auditar** (código limpo e testável).
