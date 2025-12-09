# Checklist do Projeto - Padrões de Projeto & Banco de Dados 2

## 📅 Fase 1: Planejamento e Documentação Inicial
- [x] Analisar estrutura atual e requisitos
- [ ] Criar documento de "Antes" (Snapshot da arquitetura atual) <!-- id: 0 -->
- [x] Definir tecnologias de Banco de Dados (SQL, NoSQL, Cache) <!-- id: 1 -->
- [x] Configurar ambiente Docker (Postgres, Mongo, Redis)

## 💾 Fase 2: Persistência Poliglota (Banco de Dados 2)
- [x] **Configuração JPA (SQL)** <!-- id: 2 -->
    - [x] Configurar conexão (Hibernate/Persistence.xml)
    - [x] Mapear Entidades: `Tarefa`, `Usuario`, `Evento`
    - [x] Remover mapeamento JPA de `Subtarefa` (será migrado)
## 🧩 Fase 3: Refatoração e Padrões de Projeto (GoF/SOLID/GRASP)
- [ ] **Padrões Criacionais** <!-- id: 6 -->
    - [x] Revisar/Documentar `Factory Method` (ServiceFactory)
    - [x] Implementar `Singleton` (Gerenciador de Conexões DB)
    - [x] Implementar `Builder` (Para construção complexa de Tarefas)
- [ ] **Padrões Estruturais** <!-- id: 7 -->
    - [x] Documentar `Facade` (Classe `ToDoList`)
    - [ ] Implementar `Adapter` (Se necessário para drivers de DB)
    - [ ] Implementar `Decorator` (Para validações extras ou logging)
- [ ] **Padrões Comportamentais** <!-- id: 8 -->
    - [x] Implementar `Observer` (Notificar usuário quando tarefa vence)
    - [x] Implementar `Strategy` (Ordenação e Filtros de Tarefas)
    - [x] **Limpeza Geral**: Remoção de classes legadas (`Persistencia`, `Manipulador`) e métodos mortos.
    - [x] **Template Method**: Refatorar `GeradorDeRelatorios` (`RelatorioTemplate`, `RelatorioPDF`, `RelatorioCSV`).

## 🎨 Fase 4: Modernização da Interface (Swing)
- [ ] **Look and Feel Moderno**
    - [ ] Adicionar biblioteca **FlatLaf** (Temas Dark/Light estilo IntelliJ/VSCode).
- [x] **Interface Reativa (Observer)**
    - [x] Fazer `TelaListarTarefas` implementar `IObserver` para atualizar tabela automaticamente.
- [x] **Simplificação UI**
    - [x] Refatorar `TelaLogin` para usar `BorderLayout` (Simplificação).
- [x] **Padronização Arquitetural**
    - [x] Corrigir violações do Facade em `TelaSubtarefas` e `TelaGerenciadorTarefas` (eliminar `getService()` diretos).
    - [x] Adicionar `editarSubtarefa` no `ToDoList`.

## 📚 Fase 5: Apresentação e Documentação Final
- [x] Criar documento "Depois" (Arquitetura Final) <!-- id: 9 -->
- [x] Elaborar comparativo "Antes vs Depois" (Justificativas) <!-- id: 10 -->
- [x] Preparar slides/diagramas explicativos <!-- id: 11 -->
