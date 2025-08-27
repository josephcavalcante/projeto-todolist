# Refatoração SOLID - ToDoList

## Análise do Código Atual

### Problemas Identificados

1. **SRP (Single Responsibility Principle)** - Violado
   - `ToDoList` faz muitas coisas: gerencia tarefas, persistência, relatórios, usuários
   - `TarefaService` mistura validação com lógica de negócio

2. **OCP (Open/Closed Principle)** - Violado
   - Classes não são extensíveis sem modificação
   - Lógica de relatórios hardcoded

3. **LSP (Liskov Substitution Principle)** - OK
   - Não há hierarquias problemáticas

4. **ISP (Interface Segregation Principle)** - Violado
   - Falta de interfaces específicas
   - Clientes dependem de métodos que não usam

5. **DIP (Dependency Inversion Principle)** - Violado
   - Dependências diretas de classes concretas
   - Sem inversão de controle

## Refatoração Proposta

### 1. Criação de Interfaces (ISP + DIP)

#### ITarefaRepository
```java
public interface ITarefaRepository {
    void salvar(Tarefa tarefa);
    void remover(Tarefa tarefa);
    void atualizar(Tarefa antiga, Tarefa nova);
    List<Tarefa> listarTodas();
    Tarefa buscarPorTitulo(String titulo);
}
```

#### IRelatorioService
```java
public interface IRelatorioService {
    boolean gerarPDF(List<Tarefa> tarefas, LocalDate data);
    boolean gerarExcel(List<Tarefa> tarefas, int mes, int ano);
}
```

#### INotificacaoService
```java
public interface INotificacaoService {
    boolean enviarEmail(String destinatario, String assunto, String conteudo);
}
```

### 2. Separação de Responsabilidades (SRP)

#### TarefaController
- Apenas coordena operações de tarefa
- Delega validação para ValidadorTarefa
- Delega persistência para ITarefaRepository

#### ValidadorTarefa
- Responsável apenas por validações
- Regras de negócio centralizadas

#### UsuarioService
- Gerencia apenas dados do usuário
- Separado da lógica de tarefas

### 3. Extensibilidade (OCP)

#### RelatorioFactory
- Permite adicionar novos tipos de relatório
- Sem modificar código existente

#### NotificacaoFactory
- Suporte a diferentes tipos de notificação
- Email, SMS, Push, etc.

## Implementação das Melhorias

### ✅ Concluído

1. **Interfaces Criadas (ISP + DIP)**
   - `ITarefaRepository` - Operações de persistência
   - `IValidadorTarefa` - Validações específicas
   - `IRelatorioService` - Geração de relatórios

2. **Validador Implementado (SRP)**
   - `ValidadorTarefa` - Responsabilidade única de validação
   - Separação clara das regras de negócio

3. **TarefaService Refatorado (SRP + DIP)**
   - Usa `IValidadorTarefa` em vez de validação inline
   - Responsabilidade focada em coordenação
   - Dependência de abstração, não implementação

### 🔄 Próximos Passos

1. Implementar `TarefaRepository` usando `ITarefaRepository`
2. Refatorar `ToDoList` para usar as interfaces
3. Implementar `RelatorioService`
4. Aplicar Factory Pattern para extensibilidade (OCP)
