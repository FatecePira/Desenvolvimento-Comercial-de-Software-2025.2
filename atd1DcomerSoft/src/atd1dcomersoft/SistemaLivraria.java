/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package atd1dcomersoft;

/**
 *
 * @author yasmi
 */
import java.util.*;
import java.util.Scanner;

class SistemaLivraria {
private CatalogoLivros catalogo;
private List<Cliente> clientes;
private List<Administrador> administradores;
private Relatorio relatorio;
private Scanner scanner;

public SistemaLivraria() {
    this.catalogo = new CatalogoLivros();
    this.clientes = new ArrayList<>();
    this.administradores = new ArrayList<>();
    this.relatorio = new Relatorio(catalogo, clientes);
    this.scanner = new Scanner(System.in);

    // Dados iniciais para demonstração
    inicializarDadosDemo();
}

/**
 * Inicializa o sistema com dados de demonstração
 */
private void inicializarDadosDemo() {
    // Cadastrar administrador padrão
    administradores.add(new Administrador("Admin Sistema", "admin@livraria.com", "000.000.000-00"));

    // Cadastrar alguns livros
    catalogo.adicionarLivro(new Livro("Clean Code", "Robert C. Martin", 89.90, "Programação", 15));
    catalogo.adicionarLivro(new Livro("Design Patterns", "Gang of Four", 95.50, "Programação", 8));
    catalogo.adicionarLivro(new Livro("O Hobbit", "J.R.R. Tolkien", 45.90, "Fantasia", 20));
    catalogo.adicionarLivro(new Livro("1984", "George Orwell", 39.90, "Ficção", 12));
    catalogo.adicionarLivro(new Livro("Dom Casmurro", "Machado de Assis", 29.90, "Literatura", 25));

    // Cadastrar alguns clientes
    clientes.add(new Cliente("João Silva", "joao@email.com", "123.456.789-10"));
    clientes.add(new Cliente("Maria Santos", "maria@email.com", "987.654.321-00"));
}

/**
 * Menu principal do sistema
 */
public void menuPrincipal() {
    while (true) {
        System.out.println("\\n=== SISTEMA DE GESTÃO - LIVRARIA ONLINE ===");
        System.out.println("1. Área do Cliente");
        System.out.println("2. Área do Administrador");
        System.out.println("3. Relatórios");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha

        switch (opcao) {
            case 1 -> menuCliente();
            case 2 -> menuAdministrador();
            case 3 -> menuRelatorios();
            case 4 -> {
                System.out.println("Obrigado por usar o sistema!");
                return;
            }
            default -> System.out.println("Opção inválida!");
        }
    }
}

/**
 * Menu da área do cliente
 */
private void menuCliente() {
    System.out.println("\\n=== ÁREA DO CLIENTE ===");
    System.out.println("1. Cadastrar novo cliente");
    System.out.println("2. Fazer compra");
    System.out.println("3. Consultar catálogo");
    System.out.println("4. Buscar livros");
    System.out.println("5. Voltar");
    System.out.print("Escolha uma opção: ");

    int opcao = scanner.nextInt();
    scanner.nextLine();

    switch (opcao) {
        case 1 -> cadastrarCliente();
        case 2 -> processarCompra();
        case 3 -> catalogo.exibirCatalogo();
        case 4 -> buscarLivros();
        case 5 -> { return; }
        default -> System.out.println("Opção inválida!");
    }
}

/**
 * Cadastra um novo cliente
 */
private void cadastrarCliente() {
    System.out.print("Nome: ");
    String nome = scanner.nextLine();
    System.out.print("E-mail: ");
    String email = scanner.nextLine();
    System.out.print("CPF: ");
    String cpf = scanner.nextLine();

    Cliente novoCliente = new Cliente(nome, email, cpf);
    clientes.add(novoCliente);
    System.out.println("Cliente cadastrado com sucesso!");
}

/**
 * Processa uma compra
 */
private void processarCompra() {
    if (clientes.isEmpty()) {
        System.out.println("Nenhum cliente cadastrado!");
        return;
    }

    // Selecionar cliente
    System.out.println("Selecione o cliente:");
    for (int i = 0; i < clientes.size(); i++) {
        System.out.printf("%d. %s%n", i + 1, clientes.get(i).getNome());
    }

    int clienteIndex = scanner.nextInt() - 1;
    scanner.nextLine();

    if (clienteIndex < 0 || clienteIndex >= clientes.size()) {
        System.out.println("Cliente inválido!");
        return;
    }

    Cliente cliente = clientes.get(clienteIndex);
    Compra compra = new Compra(cliente);

    // Adicionar itens ao carrinho
    while (true) {
        catalogo.exibirCatalogo();
        System.out.print("Selecione o livro (0 para finalizar): ");
        int livroIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (livroIndex == -1) break;

        Livro livro = catalogo.getLivro(livroIndex);
        if (livro == null) {
            System.out.println("Livro inválido!");
            continue;
        }

        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        if (!livro.temEstoque(quantidade)) {
            System.out.println("Estoque insuficiente! Disponível: " + livro.getQuantidadeEstoque());
            continue;
        }

        compra.adicionarItem(new ItemCarrinho(livro, quantidade));
        System.out.println("Item adicionado ao carrinho!");
    }

    if (compra.getItens().isEmpty()) {
        System.out.println("Nenhum item no carrinho!");
        return;
    }

    compra.exibirDetalhes();
    System.out.print("Confirmar compra? (s/n): ");
    String confirmacao = scanner.nextLine();

    if (confirmacao.equalsIgnoreCase("s")) {
        compra.finalizarCompra();
    } else {
        System.out.println("Compra cancelada!");
    }
}

/**
 * Busca livros no catálogo
 */
private void buscarLivros() {
    System.out.println("Buscar por:");
    System.out.println("1. Título");
    System.out.println("2. Autor");
    System.out.println("3. Categoria");
    System.out.print("Escolha: ");

    int opcao = scanner.nextInt();
    scanner.nextLine();

    String tipo = switch (opcao) {
        case 1 -> "titulo";
        case 2 -> "autor";
        case 3 -> "categoria";
        default -> "";
    };

    if (tipo.isEmpty()) {
        System.out.println("Opção inválida!");
        return;
    }

    System.out.print("Digite o termo de busca: ");
    String termo = scanner.nextLine();

    List<Livro> resultados = catalogo.buscarLivros(termo, tipo);

    if (resultados.isEmpty()) {
        System.out.println("Nenhum livro encontrado!");
    } else {
        System.out.println("=== RESULTADOS DA BUSCA ===");
        for (int i = 0; i < resultados.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, resultados.get(i));
        }
    }
}

/**
 * Menu da área do administrador
 */
private void menuAdministrador() {
    System.out.println("\\n=== ÁREA DO ADMINISTRADOR ===");
    System.out.println("1. Adicionar livro ao catálogo");
    System.out.println("2. Exibir catálogo completo");
    System.out.println("3. Listar clientes");
    System.out.println("4. Voltar");
    System.out.print("Escolha uma opção: ");

    int opcao = scanner.nextInt();
    scanner.nextLine();

    switch (opcao) {
        case 1 -> adicionarLivro();
        case 2 -> catalogo.exibirCatalogo();
        case 3 -> listarClientes();
        case 4 -> { return; }
        default -> System.out.println("Opção inválida!");
    }
}

/**
 * Adiciona um novo livro ao catálogo
 */
private void adicionarLivro() {
    System.out.print("Título: ");
    String titulo = scanner.nextLine();
    System.out.print("Autor: ");
    String autor = scanner.nextLine();
    System.out.print("Preço: ");
    double preco = scanner.nextDouble();
    scanner.nextLine();
    System.out.print("Categoria: ");
    String categoria = scanner.nextLine();
    System.out.print("Quantidade em estoque: ");
    int estoque = scanner.nextInt();
    scanner.nextLine();

    Livro novoLivro = new Livro(titulo, autor, preco, categoria, estoque);
    administradores.get(0).adicionarLivroAoCatalogo(catalogo, novoLivro);
}

/**
 * Lista todos os clientes cadastrados
 */
private void listarClientes() {
    System.out.println("=== CLIENTES CADASTRADOS ===");
    if (clientes.isEmpty()) {
        System.out.println("Nenhum cliente cadastrado.");
    } else {
        for (Cliente cliente : clientes) {
            cliente.exibirInformacoes();
            System.out.println("--------------------------");
        }
    }
}

/**
 * Menu de relatórios
 */
private void menuRelatorios() {
    System.out.println("\\n=== RELATÓRIOS ===");
    System.out.println("1. Livros mais vendidos");
    System.out.println("2. Clientes que mais compraram");
    System.out.println("3. Ambos os relatórios");
    System.out.println("4. Voltar");
    System.out.print("Escolha uma opção: ");

    int opcao = scanner.nextInt();
    scanner.nextLine();

    switch (opcao) {
        case 1 -> relatorio.livrosMaisVendidos();
        case 2 -> relatorio.clientesMaisCompraram();
        case 3 -> {
            relatorio.livrosMaisVendidos();
            relatorio.clientesMaisCompraram();
        }
        case 4 -> { return; }
        default -> System.out.println("Opção inválida!");
    }
}
}

