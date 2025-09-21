# Sistema de Gestão de Livraria Online

## Descrição
Sistema de software orientado a objetos em Java que simula uma livraria online, implementando todos os conceitos de POO, padrões de projeto e boas práticas de codificação.

## Funcionalidades Implementadas

### ✅ Requisitos Funcionais Atendidos

#### 1. Cadastro de Usuários
- **Clientes**: nome, e-mail, CPF e histórico de compras
- **Administradores**: responsáveis pela gestão do catálogo
- Aplicação de **herança** (Cliente e Administrador herdam de Usuario)
- Aplicação de **polimorfismo** (método toString() diferente para cada tipo)

#### 2. Catálogo de Livros
- Cadastro com: título, autor, preço, categoria e quantidade em estoque
- Busca por título, autor ou categoria
- **Encapsulamento** completo dos dados

#### 3. Processo de Compra
- Carrinho de compras com adição/remoção de livros
- Cálculo automático do valor total
- Atualização automática do estoque após finalização
- Validação de estoque disponível

#### 4. Relatórios
- Livros mais vendidos (ranking por quantidade)
- Clientes que mais compraram (por valor gasto)
- Estatísticas gerais de vendas

### ✅ Requisitos Não Funcionais Atendidos

#### Conceitos de Orientação a Objetos
- **Encapsulamento**: Todos os atributos são privados com getters/setters
- **Herança**: Usuario → Cliente/Administrador
- **Polimorfismo**: Diferentes implementações de toString(), métodos abstratos
- **Abstração**: Classe Usuario abstrata, interfaces bem definidas

#### Padrões de Projeto
- **Singleton**: SistemaLivraria (instância única do sistema)
- **Composition**: Cliente tem CarrinhoCompras, SistemaLivraria tem CatalogoLivros

#### Boas Práticas
- Comentários Javadoc em todas as classes e métodos
- Validações de entrada de dados
- Tratamento de casos excepcionais
- Código organizado e modular

## Estrutura do Projeto

```
src/main/java/org/example/
├── Usuario.java (classe abstrata base)
├── Cliente.java (herda de Usuario)
├── Administrador.java (herda de Usuario)
├── Livro.java (entidade básica)
├── Categoria.java (entidade básica)
├── CarrinhoCompras.java (composição com Cliente)
├── Compra.java (transação de venda)
├── CatalogoLivros.java (gerenciamento de livros)
├── RelatorioVendas.java (análise de dados)
├── SistemaLivraria.java (padrão Singleton, classe principal)
├── Main.java (demonstração completa)
└── Model/
    └── TipoUsuario.java (enum)
```

## Como Executar

### Pré-requisitos
- Java 21 ou superior
- Maven (opcional)

### Compilação e Execução
```bash
# Criar diretório para classes compiladas
mkdir target\classes

# Compilar o projeto
javac -d target\classes -cp src\main\java src\main\java\org\example\*.java src\main\java\org\example\Model\*.java

# Executar o sistema
java -cp target\classes org.example.view.Main
```

## Demonstração das Funcionalidades

O sistema demonstra automaticamente:

1. **Cadastro de Usuários**: Criação de clientes e administradores
2. **Catálogo**: Busca de livros por título, autor e categoria
3. **Processo de Compra**: Adição ao carrinho, cálculo de totais, atualização de estoque
4. **Relatórios**: Livros mais vendidos e clientes que mais compraram
5. **Polimorfismo**: Diferentes implementações do método toString()

## Resultados da Execução

O sistema processa com sucesso:
- ✅ 4 clientes cadastrados
- ✅ 2 administradores cadastrados  
- ✅ 5 livros no catálogo
- ✅ 2 compras realizadas
- ✅ Faturamento total: R$ 234,30
- ✅ Atualização automática de estoque
- ✅ Relatórios detalhados de vendas

## Conceitos POO Demonstrados

### Encapsulamento
- Atributos privados em todas as classes
- Métodos públicos para acesso controlado
- Validações nos setters

### Herança
```java
public abstract class Usuario { ... }
public class Cliente extends Usuario { ... }
public class Administrador extends Usuario { ... }
```

### Polimorfismo
```java
// Diferentes implementações do toString()
Cliente: "João Santos (joao@email.com) - Total gasto: R$ 209,40"
Administrador: "Admin Silva (admin@livraria.com)"
```

### Abstração
- Classe Usuario abstrata
- Interfaces bem definidas para cada componente
- Separação clara de responsabilidades

## Arquitetura do Sistema

O sistema foi projetado para ser:
- **Modular**: Cada classe tem uma responsabilidade específica
- **Extensível**: Fácil adição de novas funcionalidades
- **Manutenível**: Código bem documentado e organizado
- **Escalável**: Preparado para evolução futura

Este projeto demonstra a aplicação prática de todos os conceitos fundamentais da orientação a objetos em um cenário realista de desenvolvimento de software.
