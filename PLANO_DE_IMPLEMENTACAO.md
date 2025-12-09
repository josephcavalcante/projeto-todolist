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

#### 2.3. Redis (Cache de Performance) - **FEITO (Merge)**
**O que fazer**: Acelerar a leitura de tarefas.
- **Teoria**: Cache-Aside (Ler do cache; se não tiver, ler do banco e salvar no cache).
- **Ação**:
    1.  Subir um Redis (já no Docker).
    2.  Criar um `TarefaCacheRepository`.
    3.  No `TarefaService`, invalidar cache ao escrever e ler da memória do Usuário ao listar.

### Etapa 3: Segurança - **FEITO (Merge)**
**Objetivo**: Proteger o acesso.
- **Ação**:
    1.  Refinar fluxo de Login/Sessão.
    2.  Falta apenas: Hashing de Senha (BCrypt).

### Etapa 4: Refatoração com Padrões de Projeto (GoF)

Aqui aplicaremos padrões específicos para resolver problemas que surgirão com a nova arquitetura.

#### 4.3. Strategy (Filtros e Otimização) - **FEITO**
- **Melhoria**: Otimizações de leitura (RAM vs SQL) agora são tratadas transparentemente pelo método `listar(strategy, usuario)`.
- **Padrão**: O Service decide a fonte (Contexto) e a Strategy define o filtro.

#### 4.4. Singleton (Gerenciador de Conexões)
- **Problema**: Abrir conexão com Mongo/Redis toda hora é custoso.
- **Solução**: Criar `DatabaseManager` como Singleton para manter as conexões vivas.

#### 4.2. Builder (Construção de Objetos) - **FEITO**
- **Problema**: `Tarefa` possui muitos atributos obrigatórios e opcionais.
- **Solução**: `TarefaBuilder` implementado e integrado no `TarefaService`.
    ```java
    Tarefa t = new TarefaBuilder()
        .comTitulo("X")
        .comDescricao("Y")
        .comPrazo(data)
        .comPrioridade(1)
        .construir();
    - `TarefaService` implementa `ISubject` e notifica observadores.
- **Solução (Frontend - A Fazer)**:
    - `TelaPrincipal` (ou `TelaListarTarefas`) deve implementar `IObserver`.
    - Ao receber notificação `atualizar()`, a tela recarrega a tabela de tarefas.

### Etapa 5: Finalização e Apresentação
1.  **Diagrama de Classes V2**: O novo sistema.
2.  **Comparativo**:
    - "Antes: Tudo no SQL, lento para leitura." -> "Depois: Cache Redis, leitura instantânea."
    - "Antes: Construtor gigante." -> "Depois: Builder fluente."
3.  **Demo**: Mostrar o sistema rodando com os 3 bancos conectados.

## 🛠️ Próximos Passos Imediatos (Para eu executar)

1.  **[FEITO]** Criar arquivo `docker-compose.yml`.
2.  **[FEITO]** Configurar dependências no `build.gradle`.
3.  **[FEITO]** Validar execução inicial (Postgres conectado).

## 🧹 Fase Extra: Limpeza de Legado (Atual)
- **Eliminar Persistência em Arquivo**:
    - [x] Remover `Persistencia.java` e interfaces associadas.
    - [x] Remover `PersistenciaController` (não utilizado).
    - [x] Limpar `ServiceFactory` (remoção de métodos mortos).
    - [x] Remover `salvarDados()` do Facade e das Telas.
