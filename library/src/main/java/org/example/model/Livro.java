package org.example.model;

public class Livro {
    private String titulo;
    private String autor;
    private Categoria categoria;
    private double preco;
    private int qtdeEstoque;

    public Livro(String titulo, String autor, double preco, Categoria categoria, int qtdeEstoque) {
        this.titulo = titulo;
        this.autor = autor;
        this.preco = preco;
        this.categoria = categoria;
        this.qtdeEstoque = qtdeEstoque;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getQtdeEstoque() {
        return qtdeEstoque;
    }

    public void setQtdeEstoque(int qtdeEstoque) {
        this.qtdeEstoque = qtdeEstoque;
    }

    @Override
    public String toString() {
        return String.format("Título: %s | Autor: %s | Categoria: %s | Preço: R$ %.2f | Estoque: %d",
                titulo, autor, categoria != null ? categoria.getNome() : "N/A", preco, qtdeEstoque);
    }
}
