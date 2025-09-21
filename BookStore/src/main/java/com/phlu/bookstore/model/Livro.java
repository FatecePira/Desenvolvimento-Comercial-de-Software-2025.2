package com.phlu.bookstore.model;

public class Livro {

    private int id; // gerado automaticamente no DB
    private String titulo;
    private String autor;
    private double preco;
    private String categoria;
    private int quantidadeEstoque;

    public Livro(String titulo, String autor, double preco, String categoria, int quantidadeEstoque) {
        this.titulo = titulo;
        this.autor = autor;
        this.preco = preco;
        this.categoria = categoria;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Livro(int id, String titulo, String autor, double preco, String categoria, int quantidadeEstoque) {
        this(titulo, autor, preco, categoria, quantidadeEstoque);
        this.id = id;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public double getPreco() { return preco; }
    public String getCategoria() { return categoria; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
}
