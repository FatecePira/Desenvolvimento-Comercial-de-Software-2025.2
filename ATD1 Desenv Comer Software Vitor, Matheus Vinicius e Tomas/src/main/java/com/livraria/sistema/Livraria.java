package com.livraria.sistema;

import com.livraria.catalogo.Livro;
import com.livraria.usuarios.Usuario;
import com.livraria.usuarios.Cliente;
import com.livraria.usuarios.Administrador;
import com.livraria.vendas.CarrinhoDeCompras;
import com.livraria.vendas.Pedido;
import com.livraria.vendas.ItemCarrinho;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Livraria {
    private List<Usuario> usuarios;
    private List<Livro> catalogo;
    private List<Pedido> pedidos;

    public Livraria() {
        this.usuarios = new ArrayList<>();
        this.catalogo = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    // Métodos de Cadastro de Usuários
    public void cadastrarCliente(String nome, String email, String cpf) {
        if (usuarios.stream().anyMatch(u -> u.getCpf().equals(cpf))) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        usuarios.add(new Cliente(nome, email, cpf));
        System.out.println("Cliente " + nome + " cadastrado com sucesso.");
    }

    public void cadastrarAdministrador(String nome, String email, String cpf) {
        if (usuarios.stream().anyMatch(u -> u.getCpf().equals(cpf))) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        usuarios.add(new Administrador(nome, email, cpf));
        System.out.println("Administrador " + nome + " cadastrado com sucesso.");
    }

    public Optional<Usuario> buscarUsuarioPorCpf(String cpf) {
        return usuarios.stream()
                .filter(u -> u.getCpf().equals(cpf))
                .findFirst();
    }

    // Métodos de Catálogo de Livros
    public void adicionarLivroAoCatalogo(Livro livro) {
        if (catalogo.stream().anyMatch(l -> l.getTitulo().equalsIgnoreCase(livro.getTitulo()) && l.getAutor().equalsIgnoreCase(livro.getAutor()))) {
            throw new IllegalArgumentException("Livro com o mesmo título e autor já existe no catálogo.");
        }
        catalogo.add(livro);
        System.out.println("Livro \"" + livro.getTitulo() + "\" adicionado ao catálogo.");
    }

    public List<Livro> buscarLivros(String termoBusca) {
        return catalogo.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(termoBusca.toLowerCase()) ||
                             l.getAutor().toLowerCase().contains(termoBusca.toLowerCase()) ||
                             l.getCategoria().name().toLowerCase().contains(termoBusca.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Optional<Livro> buscarLivroPorTitulo(String titulo) {
        return catalogo.stream()
                .filter(l -> l.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();
    }

    // Processo de Compra
    public Pedido finalizarCompra(Cliente cliente, CarrinhoDeCompras carrinho) {
        if (carrinho.getItens().isEmpty()) {
            throw new IllegalArgumentException("Carrinho de compras vazio.");
        }

        // Verificar e atualizar estoque
        for (ItemCarrinho item : carrinho.getItens()) {
            Livro livro = item.getLivro();
            int quantidadeComprada = item.getQuantidade();
            if (livro.getEstoque() < quantidadeComprada) {
                throw new IllegalStateException("Estoque insuficiente para o livro: " + livro.getTitulo());
            }
            livro.atualizarEstoque(-quantidadeComprada); // Reduz o estoque
        }

        String idPedido = UUID.randomUUID().toString();
        Pedido novoPedido = new Pedido(idPedido, cliente, new ArrayList<>(carrinho.getItens()), carrinho.calcularTotal());
        pedidos.add(novoPedido);
        cliente.adicionarCompraAoHistorico(novoPedido);
        carrinho.limparCarrinho();
        System.out.println("Compra finalizada com sucesso! Pedido ID: " + idPedido);
        return novoPedido;
    }

    // Getters para listas (para relatórios e testes)
    public List<Livro> getCatalogo() {
        return catalogo;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}


