# Plano de Evolução do Projeto: Padrões de Projeto & Banco de Dados 2

Este documento detalha o roteiro passo a passo para transformar o projeto atual, atendendo aos requisitos das disciplinas de Padrões de Projeto e Banco de Dados 2.

## 🎯 Objetivos

1.  **Padrões de Projeto**: Aplicar SOLID, GRASP e GoF, documentando as decisões "Antes vs Depois".
2.  **Banco de Dados 2**: Implementar persistência poliglota (SQL + NoSQL + Cache) e segurança.

---

## 📅 Cronograma Didático

### Etapa 1: Preparação e Documentação "Antes"
**Objetivo**: Criar a base para a comparação na apresentação final.
1.  **Snapshot Atual**: Tirar prints do código atual (especialmente `ToDoList.java`, `Tarefa.java` com JPA, e a falta de segurança).
2.  **Diagrama de Classes V1**: Desenhar como o sistema está hoje.
3.  **Justificativa**: Por que mudar? (Ex: "Subtarefas crescem muito e não precisam de transações complexas -> Mongo", "Tarefas são lidas frequentemente -> Redis").

### Etapa 2: Persistência Poliglota (O "Core" de BD2)

#### 2.1. SQL com JPA (Entidades Relacionais)
**O que fazer**: Configurar um banco relacional real (PostgreSQL ou MySQL) via Docker ou local.
- **Ação**: Configurar `persistence.xml` (ou `hibernate.cfg.xml`) para conectar no banco.
- **Código**: Garantir que `Tarefa`, `Usuario` e `Evento` estejam mapeados corretamente.
- **Refatoração Crítica**: Eliminar a classe `ManipuladorDeTarefas`.
    - Atualmente ela age como um "Banco de Dados em Memória".
    - Sua lógica deve ser movida para `TarefaRepository` (acesso a dados) e `TarefaService` (regras de negócio).
- **Padrão**: *Repository Pattern* (já iniciado, mas agora conectando no banco real).

#### 2.2. MongoDB (Subtarefas)
**O que fazer**: Mover `Subtarefa` do SQL para o Mongo.
- **Teoria**: Explicar que documentos aninhados ou coleções separadas no Mongo oferecem flexibilidade.
- **Ação**:
    1.  Remover anotações `@Entity`, `@ManyToOne` de `Subtarefa`.
    2.  Criar `SubtarefaRepositoryMongo`.
    3.  No `TarefaService`, ao buscar uma tarefa, buscar suas subtarefas no Mongo usando o ID da Tarefa.

#### 2.3. Redis (Cache de Performance)
**O que fazer**: Acelerar a leitura de tarefas.
- **Teoria**: Cache-Aside (Ler do cache; se não tiver, ler do banco e salvar no cache).
- **Ação**:
    1.  Subir um Redis.
    2.  Criar um `CacheService` (Singleton).
    3.  No `TarefaService.listarTodas()`, verificar o Redis antes do SQL.
    4.  Ao salvar/editar uma tarefa, invalidar o cache.

### Etapa 3: Segurança
**Objetivo**: Proteger o acesso.
- **Ação**:
    1.  Adicionar biblioteca `BCrypt`.
    2.  No cadastro de usuário, salvar `hash(senha)` no banco, não a senha pura.
    3.  No login, comparar `hash(senha_digitada)` com o banco.

### Etapa 4: Refatoração com Padrões de Projeto (GoF)

Aqui aplicaremos padrões específicos para resolver problemas que surgirão com a nova arquitetura.

#### 4.1. Singleton (Gerenciador de Conexões)
- **Problema**: Abrir conexão com Mongo/Redis toda hora é custoso.
- **Solução**: Criar `DatabaseManager` como Singleton para manter as conexões vivas.

#### 4.2. Builder (Construção de Objetos)
- **Problema**: Criar uma `Tarefa` com muitos atributos opcionais (descrição, prioridade, subtarefas) fica confuso no construtor.
- **Solução**: Implementar `TarefaBuilder`.
    ```java
    Tarefa t = new TarefaBuilder().comTitulo("X").comPrioridade(1).build();
    ```

#### 4.3. Strategy (Ordenação/Filtros)
- **Problema**: Vários `if/else` para ordenar tarefas (por data, por prioridade, por nome).
- **Solução**: Interface `OrdenacaoStrategy`.
    - `OrdenacaoPorData`
    - `OrdenacaoPorPrioridade`

#### 4.4. Observer (Notificações e Interface Gráfica)
- **Problema 1**: Quando uma tarefa é concluída ou vence, queremos mandar e-mail, logar ou atualizar cache.
- **Problema 2 (Swing)**: A interface gráfica (Swing) está acoplada e precisa saber quando os dados mudam para se redesenhar.
- **Solução**: Implementar `Observer Pattern`.
    - `TarefaSubject` notifica `EmailObserver`, `LogObserver`.
    - `TelaPrincipal` implementa `Observer` e se atualiza automaticamente quando o `TarefaService` notifica uma mudança. Isso desacopla o Swing da lógica de negócio.

### Etapa 5: Finalização e Apresentação
1.  **Diagrama de Classes V2**: O novo sistema.
2.  **Comparativo**:
    - "Antes: Tudo no SQL, lento para leitura." -> "Depois: Cache Redis, leitura instantânea."
    - "Antes: Construtor gigante." -> "Depois: Builder fluente."
3.  **Demo**: Mostrar o sistema rodando com os 3 bancos conectados.

## 🛠️ Próximos Passos Imediatos (Para eu executar)

1.  Criar arquivo `docker-compose.yml` (para subir Postgres, Mongo e Redis facilmente).
2.  Configurar dependências no `build.gradle`.
3.  Começar a implementação da Etapa 2 (Persistência).
