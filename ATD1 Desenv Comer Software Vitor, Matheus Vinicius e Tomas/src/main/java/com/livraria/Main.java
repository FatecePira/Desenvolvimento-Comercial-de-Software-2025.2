package com.livraria;

import com.livraria.catalogo.Categoria;
import com.livraria.catalogo.Livro;
import com.livraria.sistema.GeradorDeRelatorios;
import com.livraria.sistema.Livraria;
import com.livraria.usuarios.Administrador;
import com.livraria.usuarios.Cliente;
import com.livraria.usuarios.Usuario;
import com.livraria.vendas.CarrinhoDeCompras;
import com.livraria.vendas.Pedido;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Classe principal que inicia o sistema da livraria.
 * É aqui que toda a interação com o usuário acontece.
 */
public class Main {
    // A instância da livraria que vai gerenciar tudo
    private static Livraria minhaLivraria = new Livraria();
    // Scanner para ler o que o usuário digita
    private static Scanner scanner = new Scanner(System.in);
    // Guarda o usuário que fez login no sistema
    private static Usuario usuarioLogado = null;

    /**
     * O ponto de entrada do programa.
     */
    public static void main(String[] args) {
        // Crio alguns dados fakes pra não ter que cadastrar tudo toda vez que rodo o código
        inicializarDados();

        int opcao;
        // Loop principal do sistema, fica rodando até o usuário escolher sair (opção 0)
        do {
            exibirMenuPrincipal();
            opcao = lerOpcao(); // Pega a opção do usuário

            switch (opcao) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    cadastrarNovoCliente();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close(); // Fecho o scanner pra não dar vazamento de recurso
    }

    /**
     * Cria alguns usuários e livros para teste.
     */
    private static void inicializarDados() {
        // Cadastrando um cliente e um admin na mão
        minhaLivraria.cadastrarCliente("Alice Silva", "alice@email.com", "123456789");
        minhaLivraria.cadastrarAdministrador("Carlos Admin", "carlos@admin.com", "987654321");

        // Adicionando uns livros no catálogo
        minhaLivraria.adicionarLivroAoCatalogo(new Livro("Clean Code", "Robert C. Martin", 85.50, Categoria.TECNOLOGIA, 10));
        minhaLivraria.adicionarLivroAoCatalogo(new Livro("O Senhor dos Anéis", "J.R.R. Tolkien", 63.70, Categoria.FANTASIA, 15));
        minhaLivraria.adicionarLivroAoCatalogo(new Livro("Outlander", "Diana Gabaldon", 99.90, Categoria.ROMANCE, 8));
    }

    /**
     * Mostra as primeiras opções para o usuário.
     */
    private static void exibirMenuPrincipal() {
        System.out.println("\n--- BEM-VINDO À LIVRARIA ---");
        System.out.println("1. Fazer Login");
        System.out.println("2. Cadastrar Novo Cliente");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Lida com o login do usuário. Pede o CPF e, se encontrar,
     * direciona para o menu certo (cliente ou admin).
     */
    private static void fazerLogin() {
        System.out.print("Digite seu CPF para login: ");
        String cpf = scanner.nextLine();

        // Optional é pra evitar ter que checar se é nulo (null check)
        Optional<Usuario> usuario = minhaLivraria.buscarUsuarioPorCpf(cpf);
        if (usuario.isPresent()) {
            usuarioLogado = usuario.get(); // Pego o usuário de dentro do Optional
            System.out.println("Login bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + ".");

            // Aqui rola o polimorfismo: se o objeto for do tipo Administrador, chama o menu de admin.
            // Se não, chama o de cliente.
            if (usuarioLogado instanceof Administrador) {
                menuAdministrador();
            } else {
                menuCliente();
            }
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    /**
     * Pede os dados e cria um novo cliente.
     */
    private static void cadastrarNovoCliente() {
        try {
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();

            minhaLivraria.cadastrarCliente(nome, email, cpf);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    // --- PARTE DO CLIENTE ---

    /**
     * Menu com as opções que o cliente pode fazer.
     */
    private static void menuCliente() {
        // Crio um carrinho de compras SÓ para essa sessão de login do cliente
        CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
        int opcao;
        do {
            System.out.println("\n--- MENU DO CLIENTE ---");
            System.out.println("1. Buscar Livro (por termo)");
            System.out.println("2. Listar Todos os Livros Disponíveis");
            System.out.println("3. Adicionar Livro ao Carrinho");
            System.out.println("4. Remover Livro do Carrinho");
            System.out.println("5. Ver Carrinho de Compras");
            System.out.println("6. Finalizar Compra");
            System.out.println("7. Ver Histórico de Compras");
            System.out.println("0. Deslogar");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    buscarLivros();
                    break;
                case 2:
                    listarTodosOsLivros();
                    break;
                case 3:
                    adicionarLivroAoCarrinho(carrinho);
                    break;
                case 4:
                    removerLivroDoCarrinho(carrinho);
                    break;
                case 5:
                    System.out.println(carrinho); // O toString do carrinho já formata a saída
                    break;
                case 6:
                    finalizarCompra(carrinho);
                    carrinho = new CarrinhoDeCompras(); // Limpo o carrinho pra ele poder comprar de novo
                    break;
                case 7:
                    verHistoricoDeCompras();
                    break;
                case 0:
                    usuarioLogado = null; // Limpa o usuário logado
                    System.out.println("Você foi deslogado.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    /**
     * Mostra todos os livros que estão no catálogo.
     */
    private static void listarTodosOsLivros() {
        List<Livro> catalogo = minhaLivraria.getCatalogo();
        if (catalogo.isEmpty()) {
            System.out.println("Não há livros no catálogo no momento.");
        } else {
            System.out.println("\n--- TODOS OS LIVROS DISPONÍVEIS ---");
            // Usa um forEach com lambda pra imprimir cada livro. Mais curto que um for normal.
            catalogo.forEach(System.out::println);
        }
    }

    /**
     * Pede um termo pro usuário e busca livros que batem com o termo.
     */
    private static void buscarLivros() {
        System.out.print("Digite o título, autor ou categoria para buscar: ");
        String termo = scanner.nextLine();
        List<Livro> resultados = minhaLivraria.buscarLivros(termo);
        if (resultados.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            System.out.println("\n--- RESULTADOS DA BUSCA ---");
            resultados.forEach(System.out::println);
        }
    }

    /**
     * Adiciona um item no carrinho de compras.
     */
    private static void adicionarLivroAoCarrinho(CarrinhoDeCompras carrinho) {
        System.out.print("Digite o título do livro que deseja adicionar: ");
        String titulo = scanner.nextLine();
        // Busca pra ver se o livro existe mesmo
        Optional<Livro> livroOpt = minhaLivraria.buscarLivroPorTitulo(titulo);

        if (livroOpt.isPresent()) {
            Livro livro = livroOpt.get();
            System.out.print("Digite a quantidade: ");
            int quantidade = lerOpcao();
            try {
                // Tenta adicionar no carrinho (pode dar erro de estoque)
                carrinho.adicionarItem(livro, quantidade);
                System.out.println(quantidade + "x \"" + livro.getTitulo() + "\" adicionado(s) ao carrinho.");
            } catch (IllegalArgumentException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Livro não encontrado no catálogo.");
        }
    }

    /**
     * Tira um livro do carrinho.
     */
    private static void removerLivroDoCarrinho(CarrinhoDeCompras carrinho) {
        System.out.print("Digite o título do livro que deseja remover: ");
        String titulo = scanner.nextLine();
        Optional<Livro> livroOpt = minhaLivraria.buscarLivroPorTitulo(titulo);

        if (livroOpt.isPresent()) {
            carrinho.removerItem(livroOpt.get());
            System.out.println("Livro \"" + titulo + "\" removido do carrinho, se existia.");
        } else {
            System.out.println("Livro não encontrado no catálogo.");
        }
    }


    /**
     * Pega o carrinho, valida e cria um pedido.
     */
    private static void finalizarCompra(CarrinhoDeCompras carrinho) {
        // Só finaliza a compra se for um Cliente
        if (usuarioLogado instanceof Cliente) {
            try {
                // O cast (Cliente) é pra garantir que to passando o tipo certo
                Pedido pedido = minhaLivraria.finalizarCompra((Cliente) usuarioLogado, carrinho);
                System.out.println("\n--- RESUMO DO PEDIDO ---");
                System.out.println(pedido);
            } catch (Exception e) {
                System.err.println("Não foi possível finalizar a compra: " + e.getMessage());
            }
        }
    }

    /**
     * Mostra todas as compras que o cliente já fez.
     */
    private static void verHistoricoDeCompras() {
        if (usuarioLogado instanceof Cliente) {
            Cliente cliente = (Cliente) usuarioLogado;
            List<Pedido> historico = cliente.getHistoricoDeCompras();
            if (historico.isEmpty()) {
                System.out.println("Você ainda não fez nenhuma compra.");
            } else {
                System.out.println("\n--- SEU HISTÓRICO DE COMPRAS ---");
                historico.forEach(System.out::println);
            }
        }
    }


    // --- PARTE DO ADMINISTRADOR ---

    /**
     * Menu de opções para o administrador.
     */
    private static void menuAdministrador() {
        int opcao;
        do {
            System.out.println("\n--- MENU DO ADMINISTRADOR ---");
            System.out.println("1. Adicionar Livro ao Catálogo");
            System.out.println("2. Gerar Relatório de Livros Mais Vendidos");
            System.out.println("3. Gerar Relatório de Clientes que Mais Compraram");
            System.out.println("4. Ver Catálogo Completo");
            System.out.println("5. Cadastrar Novo Administrador");
            System.out.println("0. Deslogar");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarNovoLivro();
                    break;
                case 2:
                    // Chama o método estático direto da classe de relatórios
                    GeradorDeRelatorios.gerarRelatorioLivrosMaisVendidos(minhaLivraria.getPedidos());
                    break;
                case 3:
                    GeradorDeRelatorios.gerarRelatorioClientesQueMaisCompraram(minhaLivraria.getPedidos());
                    break;
                case 4:
                    System.out.println("\n--- CATÁLOGO COMPLETO ---");
                    minhaLivraria.getCatalogo().forEach(System.out::println);
                    break;
                case 5:
                    cadastrarNovoAdministrador();
                    break;
                case 0:
                    usuarioLogado = null;
                    System.out.println("Você foi deslogado.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    /**
     * Pede os dados e cria um novo administrador (só pode ser chamado por outro admin).
     */
    private static void cadastrarNovoAdministrador() {
        try {
            System.out.print("Nome completo do novo admin: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();

            minhaLivraria.cadastrarAdministrador(nome, email, cpf);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    /**
     * Pede os dados de um novo livro e o adiciona no catálogo.
     */
    private static void adicionarNovoLivro() {
        try {
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Preço: ");
            double preco = scanner.nextDouble();
            scanner.nextLine(); // Limpa o buffer do scanner
            System.out.print("Categoria (TECNOLOGIA, FANTASIA, HISTORIA, FICCAO, BIOGRAFIA, ROMANCE): ");
            // Pega a string, joga pra maiúsculo e tenta converter pra um valor do Enum Categoria
            Categoria categoria = Categoria.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Estoque: ");
            int estoque = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer de novo

            Livro novoLivro = new Livro(titulo, autor, preco, categoria, estoque);
            minhaLivraria.adicionarLivroAoCatalogo(novoLivro);

        } catch (InputMismatchException e) {
            System.err.println("Erro: Entrada inválida para preço ou estoque. Use números.");
            scanner.nextLine(); // Limpa o buffer se o usuário digitou texto em vez de número
        } catch (IllegalArgumentException e) {
            // Pega erros como categoria inválida ou livro já existente
            System.err.println("Erro: " + e.getMessage());
        }
    }

    /**
     * Um método só pra ler a opção numérica do usuário.
     * Isso aqui evita um bug zuado do Scanner onde ele não consome o "enter" (a nova linha),
     * o que atrapalha a leitura de texto (nextLine) depois.
     */
    private static int lerOpcao() {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consome o "enter" que ficou no buffer
            return opcao;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Se o usuário digitou texto, limpa o buffer
            return -1; // Retorna um valor inválido pra cair no "default" do switch
        }
    }
}