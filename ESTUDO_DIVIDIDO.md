# 📚 Plano de Estudo Dividido: ToDoList v2.0

Para garantir que vocês dominem o sistema sem sobrecarregar ninguém, dividi o backend em dois lotes equilibrados.
**Regra**: Interfaces e Telas não precisam ser "estudadas" a fundo, apenas usadas. Foquem nas classes abaixo.

---

## 👤 Lote 1: O "Core" (Lógica Central & Banco SQL/Redis)
**Perfil do Estudante**: Deve saber explicar como o sistema funciona "por dentro". Responsável pelo fluxo principal (Login -> CRUD -> Banco).

### 🎯 Classes para Dominar:
1.  **`negocio.ToDoList.java` (Facade)**
    *   *Por que?*: É a classe principal que a tela chama. Entenda como ela delega as funções.
2.  **`controle.services.TarefaService.java`**
    *   *Por que?*: Onde a mágica acontece. Estude o método `listar` (Cache-Aside) e o `Observer`.
3.  **`controle.services.UsuarioService.java`**
    *   *Por que?*: Login e cadastro. Entenda o uso do `BCrypt` (Segurança).
4.  **`repositorios.TarefaCacheRepository.java` (Redis)**
    *   *Por que?*: A parte mais avançada de performance. Entenda como ele salva JSON no Redis.
5.  **`repositorios.TarefaRepository.java` (PostgreSQL)**
    *   *Por que?*: O JPA/Hibernate clássico.
6.  **`modelo.Tarefa.java` & `modelo.Usuario.java`**
    *   *Por que?*: As entidades do banco. Entenda as anotações `@Entity`, `@OneToMany`.

**💡 Pergunta Surpresa do Professor:**
> *"Como funciona o cache? Se eu derrubar o Redis, o sistema para?"*
> **Resp**: Não para! O `TarefaService` captura a falha e busca no PostgreSQL.

---

## 👤 Lote 2: A "Arquitetura" (Padrões Avançados & NoSQL)
**Perfil do Estudante**: Deve saber explicar a inteligência do código e os recursos "extras". Responsável por provar que o software é bem desenhado.

### 🎯 Classes para Dominar:
1.  **`relatorios.*` (`Template`, `PDF`, `CSV`)**
    *   *Por que?*: Melhor exemplo de POO (Herança/Polimorfismo) com **Template Method**.
2.  **`strategies.*` (`FiltroStrategy`, `Ordenacao`)**
    *   *Por que?*: Exemplo prático de **Strategy Pattern** para evitar IFs gigantes.
3.  **`builders.TarefaBuilder.java`**
    *   *Por que?*: Criação fluente de objetos. Explique como ele limpa o código.
4.  **`factories.ServiceFactory.java`**
    *   *Por que?*: Inversão de Controle. Quem cria os services é a fábrica, não a tela.
5.  **`repositorios.SubtarefaRepositoryMongo.java` (MongoDB)**
    *   *Por que?*: Mostra que o sistema é poliglota (SQL + NoSQL).
6.  **`persistencia.DatabaseManager.java` (Singleton)**
    *   *Por que?*: Controle de conexão única.
7.  **`comunicacao.Mensageiro.java`**
    *   *Por que?*: Integração externa (JavaMail).

**💡 Pergunta Surpresa do Professor:**
> *"Por que vocês dividiram os relatórios em várias classes?"*
> **Resp**: Para seguir o **Open/Closed Principle**. Se quisermos um relatório HTML, criamos uma classe nova sem mexer na lógica antiga.
