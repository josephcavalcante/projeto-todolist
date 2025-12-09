# ToDoList 2.0 - Sistema Avançado de Gerenciamento de Tarefas

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-8.11-blue.svg)](https://gradle.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791.svg)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248.svg)](https://www.mongodb.com/)
[![Redis](https://img.shields.io/badge/Redis-Cache-DC382D.svg)](https://redis.io/)

## 📋 Sobre o Projeto

Sistema de gerenciamento de tarefas profissional, desenvolvido como case acadêmico para as disciplinas de **Padrões de Projeto** e **Banco de Dados 2**. O projeto evoluiu de um gerenciador de arquivos simples para uma aplicação robusta com persistência poliglota, arquitetura em camadas e interface gráfica reativa.

---

## 🏗️ Arquitetura e Design Patterns

O sistema implementa rigorosamente 8 padrões de projeto (GoF/GRASP) documentados em [docs/RELATORIO_PADROES.md](docs/RELATORIO_PADROES.md).

### 🎨 Padrões Criacionais
1.  **Factory Method**: `ServiceFactory` centraliza a criação de Services, Controllers e Repositórios.
2.  **Singleton**: `DatabaseManager` garante instância única de conexões (JPA).
3.  **Builder**: `TarefaBuilder` facilita a criação de objetos complexos (Tarefas com muitos atributos).

### 🧩 Padrões Estruturais
4.  **Facade**: `ToDoList` atua como fachada única para a UI, delegando para Controllers.
5.  **Proxy**: `TarefaRepositoryProxy` intercepta chamadas ao banco para gerenciar Cache (Redis) transparentemente.

### ⚙️ Padrões Comportamentais
6.  **Observer**: `TelaListarTarefas` observa mudanças no `TarefaService` para atualizações em tempo real.
7.  **Strategy**: Diferentes estratégias de filtro e ordenação (`FiltroPorData`, `OrdenacaoPorPrioridade`).
8.  **Template Method**: `RelatorioTemplate` define o esqueleto da geração de relatórios (PDF/CSV).

---

## 🛠️ Tecnologias (Persistência Poliglota)

Aplicamos o conceito de **Persistência Poliglota**, usando o banco ideal para cada tipo de dado:

| Tecnologia | Função no Projeto | Motivo da Escolha |
| :--- | :--- | :--- |
| **PostgreSQL** | Dados Core (`Tarefas`, `Usuarios`) | Integridade referencial e transações ACID. |
| **MongoDB** | `Subtarefas` | Flexibilidade de esquema (document-oriented). |
| **Redis** | Cache de Performance | Acesso ultra-rápido para listagens frequentes. |

---

## 🚀 Funcionalidades

### ✅ Gerenciamento de Tarefas
*   CRUD Completo (Postgres + Redis Cache)
*   **Prioridades e Prazos** com validação estrita.
*   **Filtros e Ordenação** dinâmicos (Strategy Pattern).

### ✅ Subtarefas (MongoDB)
*   Adição dinâmica de itens a uma tarefa.
*   Cálculo automático de progresso (Observer Pattern atualiza a Tarefa pai).

### ✅ Gerenciador de Eventos
*   Agendamento de compromissos com local e data.
*   **Validação de Conflitos**: O sistema impede dois eventos no mesmo horário.
*   Carregamento assíncrono para não travar a UI.

### ✅ Relatórios Avançados
*   **PDF**: Geração de relatórios detalhados com iText.
*   **Excel**: Exportação para planilhas.
*   **E-mail**: Envio assíncrono de relatórios (não trava a tela enquanto envia).

---

## 📂 Estrutura de Pacotes

```
src/main/java/
├── comunicacao/          # Envio de emails (JavaMail)
├── controle/services/    # Regras de Negócio (Services)
├── controllers/          # Controladores (Camada intermediária UI-Service)
├── factories/            # Fábricas de objetos
├── interfaces/           # Contratos (Interfaces)
├── modelo/               # Entidades (JPA/POJOs)
├── negocio/              # Facade (ToDoList)
├── persistencia/         # Gerenciamento de Conexões
├── relatorios/           # Template Method (PDF/Excel)
├── repositorios/         # Acesso a Dados (DAOs / Repositories)
├── strategies/           # Algoritmos de filtro/ordenação
├── telas/                # Interface Gráfica (Swing)
└── validadores/          # Lógica de validação (SRP)
```

---

## ▶️ Como Executar

### 1. Subir Infraestrutura (Docker)
O projeto depende de bancos de dados rodando. Use o Docker Compose:
```bash
docker-compose up -d
```
*Isso subirá Postgres (5433), Mongo (27017) e Redis (6379).*

### 2. Rodar a Aplicação
```bash
# Interface Gráfica (Recomendado)
./gradlew runGUI

# Versão Console (Legado)
./gradlew run
```

---

## 🤝 Contribuição
Projeto desenvolvido para fins acadêmicos.

**Licença:** MIT