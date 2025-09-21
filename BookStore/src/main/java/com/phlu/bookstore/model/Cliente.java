package com.phlu.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private int id; // novo campo id para persistência
    private String cpf;
    private CarrinhoDeCompras carrinho;
    private List<Compra> historicoCompras;

    // Construtor completo (com id)
    public Cliente(int id, String nome, String email, String senha, String cpf, boolean hashed) {
        super(nome, email, senha, hashed);
        this.id = id;
        this.cpf = cpf;
        this.carrinho = new CarrinhoDeCompras();
        this.historicoCompras = new ArrayList<>();
    }

    // Construtor sem id (novo cliente antes de salvar no banco)
    public Cliente(String nome, String email, String senha, String cpf, boolean hashed) {
        super(nome, email, senha, hashed);
        this.cpf = cpf;
        this.carrinho = new CarrinhoDeCompras();
        this.historicoCompras = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public CarrinhoDeCompras getCarrinho() { return carrinho; }
    public void limparCarrinho() { carrinho.limparCarrinho(); }

    public void adicionarCompra(Compra compra) { historicoCompras.add(compra); }
    public List<Compra> getHistoricoCompras() { return historicoCompras; }

    @Override
    public void exibirInformacoes() {
        System.out.println("Cliente: " + getNome() + ", Email: " + getEmail() +
                ", CPF: " + cpf + ", Compras realizadas: " + historicoCompras.size());
    }
}
