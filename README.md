# ToDoList - Sistema de Gerenciamento de Tarefas

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-8.11.1-blue.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 📋 Sobre o Projeto

Sistema desktop desenvolvido em Java para gerenciamento de tarefas, aplicando padrões de projeto SOLID e GRASP. O projeto foi refatorado de uma estrutura monolítica para uma arquitetura limpa e modular.

## 🏗️ Arquitetura

### Padrões Implementados

**SOLID:**
- **SRP**: Cada classe tem uma única responsabilidade
- **OCP**: Extensível via interfaces e factories
- **LSP**: Implementações substituíveis via interfaces
- **ISP**: Interfaces específicas e segregadas
- **DIP**: Injeção de dependência via ServiceFactory

**GRASP:**
- **Information Expert**: Dados encapsulados adequadamente
- **Creator**: ServiceFactory centraliza criação
- **Controller**: Controllers coordenam operações
- **Low Coupling**: Interfaces reduzem dependências
- **High Cohesion**: Classes focadas e coesas

### Estrutura de Pacotes

```
src/main/java/
├── comunicacao/          # Envio de emails
├── controle/services/    # Serviços de negócio
├── controllers/          # Controladores (GRASP)
├── factories/           # Criação de objetos (Creator)
├── interfaces/          # Contratos (DIP + ISP)
│   ├── controllers/
│   ├── persistence/
│   ├── repositories/
│   ├── services/
│   └── validators/
├── modelo/              # Entidades de domínio
├── negocio/             # Lógica de negócio principal
├── persistencia/        # Persistência de dados
├── relatorios/          # Geração de relatórios
├── repositorios/        # Acesso a dados
├── telas/               # Interface gráfica (Swing)
└── validadores/         # Validações de entrada
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Gradle 8.10+ (ou usar o wrapper incluído)

### Executar via Gradle

```bash
# Compilar o projeto
./gradlew build

# Executar aplicação console
./gradlew run

# Executar interface gráfica
./gradlew runGUI

# Gerar JAR executável
./gradlew jar
```

### Executar JAR

```bash
# Após gerar o JAR
java -jar build/libs/projeto-todolist-2.0.jar
```

## 🛠️ Funcionalidades

### Core
- ✅ Adicionar, editar e remover tarefas
- ✅ Gerenciar subtarefas
- ✅ Sistema de prioridades (1-5)
- ✅ Controle de percentual de conclusão
- ✅ Identificação de tarefas críticas

### Relatórios
- ✅ Geração de PDF por data
- ✅ Relatório Excel mensal
- ✅ Envio por email automático

### Interface
- ✅ Interface console (Main.java)
- ✅ Interface gráfica Swing completa
- ✅ Configurações de usuário

## 📁 Estrutura de Dados

### Entidades Principais

**Tarefa**
- ID, título, descrição
- Data de cadastro e deadline
- Prioridade e percentual
- Lista de subtarefas

**Subtarefa**
- Título, descrição, percentual
- Referência à tarefa pai

**Usuário**
- Nome e email para relatórios

## 🔧 Tecnologias

- **Java 17**: Linguagem principal
- **Gradle**: Build e gerenciamento de dependências
- **Swing**: Interface gráfica
- **iText**: Geração de PDFs
- **Jakarta Mail**: Envio de emails
- **XStream**: Serialização XML
- **JUnit 5**: Testes unitários

## 📚 Dependências

```gradle
dependencies {
    implementation 'jakarta.mail:jakarta.mail-api:2.1.2'
    implementation 'org.eclipse.angus:jakarta.mail:2.0.2'
    implementation 'com.itextpdf:itextpdf:5.5.13.3'
    implementation 'jakarta.persistence:jakarta.persistence-api:3.1.0'
    implementation 'com.thoughtworks.xstream:xstream:1.4.21'
    
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.10.2'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.10.2'
}
```

## 🎯 Objetivos Acadêmicos

Este projeto demonstra:

1. **Refatoração de código legado** para arquitetura limpa
2. **Aplicação prática de SOLID e GRASP**
3. **Uso de padrões de projeto** (Factory, Repository, Service Layer)
4. **Migração para build tool moderno** (Gradle)
5. **Estruturação profissional** de projeto Java

## 📈 Evolução do Projeto

### Antes (v1.0)
- Classe monolítica com 150+ linhas
- 8+ responsabilidades misturadas
- Alto acoplamento
- Difícil manutenção

### Depois (v2.0)
- 13+ classes especializadas
- Responsabilidades bem definidas
- Baixo acoplamento via interfaces
- Fácil extensão e manutenção

## 🤝 Contribuição

Este é um projeto acadêmico para demonstração de padrões de projeto. Sugestões e melhorias são bem-vindas!

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes.

---

**Desenvolvido para a disciplina de Padrões de Projeto - Curso ADS**