package com.livraria.catalogo;

public class Livro {
    private String titulo;
    private String autor;
    private double preco;
    private Categoria categoria;
    private int estoque;

    public Livro(String titulo, String autor, double preco, Categoria categoria, int estoque) {
        this.titulo = titulo;
        this.autor = autor;
        this.preco = preco;
        this.categoria = categoria;
        this.estoque = estoque;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public double getPreco() {
        return preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getEstoque() {
        return estoque;
    }

    // Setters
    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    // Método para atualizar o estoque
    public void atualizarEstoque(int quantidade) {
        if (this.estoque + quantidade < 0) {
            throw new IllegalArgumentException("Estoque insuficiente.");
        }
        this.estoque += quantidade;
    }

    @Override
    public String toString() {
        return "Título: " + titulo + ", Autor: " + autor + ", Preço: " + preco + ", Categoria: " + categoria + ", Estoque: " + estoque;
    }
}


