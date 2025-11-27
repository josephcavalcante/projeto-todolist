# Checklist do Projeto - Padrões de Projeto & Banco de Dados 2

## 📅 Fase 1: Planejamento e Documentação Inicial
- [x] Analisar estrutura atual e requisitos
- [ ] Criar documento de "Antes" (Snapshot da arquitetura atual) <!-- id: 0 -->
- [ ] Definir tecnologias de Banco de Dados (SQL, NoSQL, Cache) <!-- id: 1 -->

## 💾 Fase 2: Persistência Poliglota (Banco de Dados 2)
- [ ] **Configuração JPA (SQL)** <!-- id: 2 -->
    - [ ] Configurar conexão (Hibernate/Persistence.xml)
    - [ ] Mapear Entidades: `Tarefa`, `Usuario`, `Evento`
    - [ ] Remover mapeamento JPA de `Subtarefa` (será migrado)
- [ ] **Integração MongoDB (NoSQL)** <!-- id: 3 -->
    - [ ] Adicionar dependência MongoDB Driver
    - [ ] Implementar `SubtarefaRepositoryMongo`
    - [ ] Refatorar `Subtarefa` para Documento Mongo
    - [ ] Implementar vínculo lógico (Tarefa ID) entre SQL e Mongo
- [ ] **Integração Redis (Cache)** <!-- id: 4 -->
    - [ ] Adicionar dependência Jedis ou Lettuce
    - [ ] Implementar `CacheService` (Singleton?)
    - [ ] Adicionar Cache-Aside no `TarefaService` (Listagem/Busca)
- [ ] **Segurança e Login** <!-- id: 5 -->
    - [ ] Implementar Hashing de Senha (BCrypt)
    - [ ] Refinar fluxo de Login/Sessão

## 🧩 Fase 3: Refatoração e Padrões de Projeto (GoF/SOLID/GRASP)
- [ ] **Padrões Criacionais** <!-- id: 6 -->
    - [ ] Revisar/Documentar `Factory Method` (ServiceFactory)
    - [ ] Implementar `Singleton` (Gerenciador de Conexões DB)
    - [ ] Implementar `Builder` (Para construção complexa de Tarefas/Relatórios)
- [ ] **Padrões Estruturais** <!-- id: 7 -->
    - [ ] Documentar `Facade` (Classe `ToDoList`)
    - [ ] Implementar `Adapter` (Se necessário para drivers de DB)
    - [ ] Implementar `Decorator` (Para validações extras ou logging)
- [ ] **Padrões Comportamentais** <!-- id: 8 -->
    - [ ] Implementar `Observer` (Notificar usuário quando tarefa vence)
    - [ ] Implementar `Strategy` (Diferentes formatos de relatório ou ordenação)
    - [ ] Implementar `Template Method` (Fluxo de persistência?)

## 📚 Fase 4: Apresentação e Documentação Final
- [ ] Criar documento "Depois" (Arquitetura Final) <!-- id: 9 -->
- [ ] Elaborar comparativo "Antes vs Depois" (Justificativas) <!-- id: 10 -->
- [ ] Preparar slides/diagramas explicativos <!-- id: 11 -->
