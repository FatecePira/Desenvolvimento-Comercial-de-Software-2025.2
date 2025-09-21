package org.example.controller;

import org.example.model.*;

import java.util.ArrayList;
import java.util.List;

public class SistemaLivraria {
    private CatalogoLivros catalogo;
    private List<Cliente> clientes;
    private List<Administrador> administradores;
    private List<Compra> historicoCompras;
    private RelatorioVendas relatorios;
    private Usuario usuario;

    public SistemaLivraria() {
        this.catalogo = new CatalogoLivros();
        this.clientes = new ArrayList<>();
        this.administradores = new ArrayList<>();
        this.historicoCompras = new ArrayList<>();
        this.relatorios = new RelatorioVendas(historicoCompras);
    }

    public Cliente cadastrarCliente(String nome, String email, String cpf) {
        // Verifica se já existe um cliente com este email
        for (Cliente cliente : clientes) {
            if (cliente.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Já existe um cliente com este email: " + email);
                return null;
            }
        }

        Cliente novoCliente = new Cliente(nome, email, cpf);
        clientes.add(novoCliente);
        System.out.println("Cliente cadastrado com sucesso: " + nome);
        return novoCliente;
    }

    // Cadastra um novo administrador
    public Administrador cadastrarAdministrador(String nome, String email, String cpf) {
        // Verifica se já existe um administrador com este email
        for (Administrador admin : administradores) {
            if (admin.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Já existe um administrador com este email: " + email);
                return null;
            }
        }

        Administrador novoAdmin = new Administrador(nome, email, cpf);
        administradores.add(novoAdmin);
        System.out.println("Administrador cadastrado com sucesso: " + nome);
        return novoAdmin;
    }

    //Busca um cliente pelo email
    public Cliente buscarCliente(String email) {
        return clientes.stream()
                .filter(cliente -> cliente.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    //Busca um administrador pelo email
    public Administrador buscarAdministrador(String email) {
        return administradores.stream()
                .filter(admin -> admin.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // Processa a compra de um cliente
    public boolean processarCompra(Cliente cliente) {
        CarrinhoCompras carrinho = cliente.getCarrinho();

        if (carrinho.isEmpty()) {
            System.out.println("Carrinho vazio. Não é possível processar a compra.");
            return false;
        }

        // Cria a compra
        Compra compra = new Compra(cliente, carrinho.getItens());

        // Processa a compra (atualiza estoque)
        if (compra.processarCompra()) {
            // Adiciona ao histórico do cliente
            cliente.adicionarCompraAoHistorico(compra);

            // Adiciona ao histórico geral
            historicoCompras.add(compra);

            // Atualiza relatórios
            relatorios.adicionarCompra(compra);

            // Limpa o carrinho
            carrinho.limpar();

            System.out.println("Compra processada com sucesso!");
            System.out.println(compra.toString());
            return true;
        } else {
            System.out.println("Erro ao processar a compra. Verifique o estoque dos itens.");
            return false;
        }
    }


    // Cadastra um novo livro no catálogo
    public Livro cadastrarLivro(String titulo, String autor, double preco, Categoria categoria, int qtdeEstoque) {
        Livro novoLivro = new Livro(titulo, autor, preco, categoria, qtdeEstoque);
        catalogo.adicionarLivro(novoLivro);
        System.out.println("Livro cadastrado com sucesso: " + novoLivro.getTitulo());
        return novoLivro;
    }

    // Getters para acesso aos componentes do sistema
    public CatalogoLivros getCatalogo() {
        return catalogo;
    }

    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes);
    }

    public List<Administrador> getAdministradores() {
        return new ArrayList<>(administradores);
    }

    public List<Compra> getHistoricoCompras() {
        return new ArrayList<>(historicoCompras);
    }

    public RelatorioVendas getRelatorios() {
        return relatorios;
    }

    public void exibirStatusSistema() {
        System.out.println("=== STATUS DO SISTEMA DA LIVRARIA ===");
        System.out.println("Clientes cadastrados: " + clientes.size());
        System.out.println("Administradores cadastrados: " + administradores.size());
        System.out.println("Livros no catálogo: " + catalogo.getTotalLivros());
        System.out.println("Compras realizadas: " + historicoCompras.size());
        System.out.println("Faturamento total: R$ " + String.format("%.2f", relatorios.getFaturamentoTotal()));
    }

    public List<Usuario> getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.addAll(clientes);
        usuarios.addAll(administradores);
        return usuarios;
    }
}
