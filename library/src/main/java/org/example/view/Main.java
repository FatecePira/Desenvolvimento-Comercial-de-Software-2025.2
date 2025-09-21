package org.example.view;

import org.example.controller.CarrinhoCompras;
import org.example.controller.CatalogoLivros;
import org.example.controller.RelatorioVendas;
import org.example.controller.SistemaLivraria;
import org.example.model.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTÃO DA LIVRARIA ONLINE ===");

        SistemaLivraria sistema = new SistemaLivraria();
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;
        while (executando) {
            System.out.println("\n=== O QUE VOCE DESEJA FAZER? SELECIONE UMA DAS OPÇÕES ABAIXO: ===");
            System.out.println("1. Cadastrar usuário/administrador");
            System.out.println("2. Buscar livros");
            System.out.println("3. Comprar Livro");
            System.out.println("4. Gerar relatório de livros mais vendidos e clientes que mais compraram");
            System.out.println("5. Exibir status do sistema");
            System.out.println("6. Cadastrar livro (apenas administrador)");
            System.out.println("7. Listar usuários (apenas administrador)");
            System.out.println("8. Sair");
            System.out.print("Opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer
            switch (opcao) {
                case 1:
                    System.out.println("Digite o tipo de usuário (1-Cliente, 2-Administrador): ");
                    int tipo = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    if (tipo == 1) {
                        Cliente cliente = sistema.cadastrarCliente(nome, email, cpf);
                        System.out.println("Cliente cadastrado: " + cliente);
                    } else if (tipo == 2) {
                        Administrador admin = sistema.cadastrarAdministrador(nome, email, cpf);
                        System.out.println("Administrador cadastrado: " + admin);
                    } else {
                        System.out.println("Tipo inválido!");
                    }
                    break;
                case 2:
                    CatalogoLivros catalogo = sistema.getCatalogo();
                    System.out.println("Buscar por: 1-Título, 2-Autor, 3-Categoria, 4-Todos");
                    int busca = scanner.nextInt();
                    scanner.nextLine();
                    switch (busca) {
                        case 1:
                            System.out.print("Digite o título: ");
                            String titulo = scanner.nextLine();
                            var livrosTitulo = catalogo.buscarPorTituloContendo(titulo);
                            if (livrosTitulo.isEmpty()) {
                                System.out.println("Nenhum livro encontrado com o título: " + titulo);
                            } else {
                                livrosTitulo.forEach(livro -> System.out.println(livro));
                            }
                            break;
                        case 2:
                            System.out.print("Digite o autor: ");
                            String autor = scanner.nextLine();
                            var livrosAutor = catalogo.buscarPorAutor(autor);
                            if (livrosAutor.isEmpty()) {
                                System.out.println("Nenhum livro encontrado para o autor: " + autor);
                            } else {
                                livrosAutor.forEach(livro -> System.out.println(livro));
                            }
                            break;
                        case 3:
                            System.out.print("Digite a categoria: ");
                            String categoriaBusca = scanner.nextLine();
                            var livrosCat = catalogo.buscarPorCategoria(categoriaBusca);
                            if (livrosCat.isEmpty()) {
                                System.out.println("Nenhum livro encontrado para a categoria: " + categoriaBusca);
                            } else {
                                livrosCat.forEach(livro -> System.out.println(livro));
                            }
                            break;
                        case 4:
                            System.out.println(catalogo.toString());
                            break;
                        default:
                            System.out.println("Opção inválida!");
                    }
                    break;
                case 3:
                    System.out.print("Email do cliente: ");
                    String emailCliente = scanner.nextLine();
                    Cliente clienteCompra = sistema.buscarCliente(emailCliente);
                    if (clienteCompra == null) {
                        System.out.println("Cliente não encontrado!");
                        break;
                    }
                    CarrinhoCompras carrinho = clienteCompra.getCarrinho();
                    boolean manipulandoCarrinho = true;
                    while (manipulandoCarrinho) {
                        System.out.println("\n1. Adicionar item ao carrinho");
                        System.out.println("2. Remover item do carrinho");
                        System.out.println("3. Finalizar e processar compra");
                        System.out.print("Escolha uma opção: ");
                        int opCarrinho = scanner.nextInt();
                        scanner.nextLine();
                        switch (opCarrinho) {
                            case 1:
                                System.out.print("Título do livro para adicionar: ");
                                String tituloLivroAdd = scanner.nextLine();
                                Livro livroAdd = sistema.getCatalogo().buscarPorTitulo(tituloLivroAdd);
                                if (livroAdd == null) {
                                    System.out.println("Livro não encontrado!");
                                    break;
                                }
                                System.out.print("Quantidade: ");
                                int qtdAdd = scanner.nextInt();
                                scanner.nextLine();
                                carrinho.adicionarItem(livroAdd, qtdAdd);
                                System.out.println("Adicionado ao carrinho: " + livroAdd.getTitulo() + " (" + qtdAdd + ")");
                                break;
                            case 2:
                                System.out.print("Título do livro para remover: ");
                                String tituloLivroRem = scanner.nextLine();
                                Livro livroRem = sistema.getCatalogo().buscarPorTitulo(tituloLivroRem);
                                if (livroRem == null) {
                                    System.out.println("Livro não encontrado!");
                                    break;
                                }
                                System.out.print("Quantidade para remover: ");
                                int qtdRem = scanner.nextInt();
                                scanner.nextLine();
                                boolean removido = carrinho.removerItem(livroRem, qtdRem);
                                if (removido) {
                                    System.out.println("Removido do carrinho: " + livroRem.getTitulo() + " (" + qtdRem + ")");
                                } else {
                                    System.out.println("Não foi possível remover. Verifique se o item está no carrinho e a quantidade.");
                                }
                                break;
                            case 3:
                                manipulandoCarrinho = false;
                                break;
                            default:
                                System.out.println("Opção inválida!");
                        }
                    }
                    System.out.println("Carrinho:");
                    System.out.println(carrinho);
                    System.out.println("Processando compra...");
                    boolean sucesso = sistema.processarCompra(clienteCompra);
                    break;
                case 4:
                    demonstrarRelatorios(sistema);
                    break;
                case 5:
                    sistema.exibirStatusSistema();
                    break;
                case 6:
                    System.out.print("Digite o email do administrador: ");
                    String emailAdmin = scanner.nextLine();
                    Administrador admin = sistema.buscarAdministrador(emailAdmin);
                    if (admin == null) {
                        System.out.println("Apenas administradores podem cadastrar livros!");
                        break;
                    }
                    System.out.print("Título do livro: ");
                    String tituloLivro = scanner.nextLine();
                    System.out.print("Autor do livro: ");
                    String autorLivro = scanner.nextLine();
                    System.out.print("Categoria do livro: ");
                    String nomeCategoria = scanner.nextLine();
                    Categoria categoriaLivro = new Categoria(nomeCategoria, "");
                    System.out.print("Preço do livro: ");
                    double precoLivro = scanner.nextDouble();
                    System.out.print("Quantidade em estoque: ");
                    int estoqueLivro = scanner.nextInt();
                    scanner.nextLine();
                    sistema.cadastrarLivro(tituloLivro, autorLivro, precoLivro, categoriaLivro, estoqueLivro);
                    System.out.println("Livro cadastrado com sucesso!");
                    break;
                case 7:
                    System.out.print("Digite o email do administrador: ");
                    String emailAdminList = scanner.nextLine();
                    Administrador adminList = sistema.buscarAdministrador(emailAdminList);
                    if (adminList == null) {
                        System.out.println("Apenas administradores podem listar usuários!");
                        break;
                    }
                    System.out.println("=== LISTA DE USUÁRIOS ===");
                    for (Usuario u : sistema.getUsuarios()) {
                        System.out.println(u);
                    }
                    break;
                case 8:
                    System.out.println("Saindo...");
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }

    private static void demonstrarRelatorios(SistemaLivraria sistema) {
        System.out.println("\n4. RELATÓRIOS DE VENDAS:");
        System.out.println("- Livros mais vendidos");
        System.out.println("- Clientes que mais compraram");
        System.out.println("- Análise de faturamento\n");

        RelatorioVendas relatorios = sistema.getRelatorios();

        // Gerar e exibir relatório completo
        String relatorioCompleto = relatorios.gerarRelatorioCompleto();
        System.out.println(relatorioCompleto);

    }
}