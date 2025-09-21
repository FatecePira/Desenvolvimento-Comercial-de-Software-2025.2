package com.phlu.bookstore.view;

import com.phlu.bookstore.model.Livro;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    // --- Mensagens ---
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }

    public void mostrarErro(String msg) {
        System.out.println("Erro: " + msg);
    }

    public String capturarEntrada() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    // --- Menus ---
    public void exibirMenuLogin() {
        System.out.println("\n--- Menu de Login / Cadastro ---");
        System.out.println("1. Fazer login");
        System.out.println("2. Cadastrar");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opcao: ");
    }

    public void exibirMenuCliente(String nome) {
        System.out.println("\n--- Menu Cliente ---");
        System.out.println("Bem-vindo, " + nome + "!");
        System.out.println("1. Visualizar catalogo");
        System.out.println("2. Visualizar carrinho");
        System.out.println("3. Visualizar compras");
        System.out.println("4. Logout");
        System.out.print("Escolha uma opcao: ");
    }

    public void exibirMenuAdministrador(String nome) {
        System.out.println("\n--- Menu Administrador ---");
        System.out.println("Bem-vindo, " + nome + "!");
        System.out.println("1. Visualizar catalogo");
        System.out.println("2. Cadastrar livro");
        System.out.println("3. Cadastrar usuario/admin");
        System.out.println("4. Relatorios");
        System.out.println("5. Logout");
        System.out.print("Escolha uma opcao: ");
    }

    // --- Livros ---
    public void exibirLivros(List<Livro> livros) {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        System.out.println("\n--- Livros Disponiveis ---");
        for (int i = 0; i < livros.size(); i++) {
            Livro l = livros.get(i);
            String status = l.getQuantidadeEstoque() == 0 ? "INDISPONIVEL" : "Disponivel: " + l.getQuantidadeEstoque();
            System.out.printf("%d. %s | %s | R$ %.2f | %s | Categoria: %s\n",
                    i + 1, l.getTitulo(), l.getAutor(), l.getPreco(), status, l.getCategoria());
        }
    }

    // --- Carrinho ---
    public void exibirCarrinho(List<Livro> carrinho) {
        if (carrinho.isEmpty()) {
            System.out.println("Carrinho vazio.");
            return;
        }

        System.out.println("\n--- Carrinho ---");
        double total = 0;
        for (int i = 0; i < carrinho.size(); i++) {
            Livro l = carrinho.get(i);
            System.out.printf("%d. %s | %s | R$ %.2f | Estoque: %d | Categoria: %s\n",
                    i + 1, l.getTitulo(), l.getAutor(), l.getPreco(), l.getQuantidadeEstoque(), l.getCategoria());
            total += l.getPreco();
        }
        System.out.printf("Total: R$ %.2f\n", total);
    }

    // --- Entradas e confirmacoes ---
    public int capturarOpcao(int min, int max) {
        while (true) {
            String input = capturarEntrada();
            try {
                int opcao = Integer.parseInt(input);
                if (opcao >= min && opcao <= max) return opcao;
                mostrarErro("Opcao fora do intervalo.");
            } catch (NumberFormatException e) {
                mostrarErro("Entrada invalida. Digite um numero.");
            }
        }
    }

    public boolean confirmar(String msg) {
        System.out.println(msg + " (S/N)");
        String resposta = capturarEntrada().trim().toUpperCase();
        return resposta.equals("S");
    }
}
