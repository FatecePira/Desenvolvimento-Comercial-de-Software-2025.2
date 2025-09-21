package com.livraria.vendas;

import com.livraria.catalogo.Livro;

public class ItemCarrinho {
    private Livro livro;
    private int quantidade;

    public ItemCarrinho(Livro livro, int quantidade) {
        this.livro = livro;
        this.quantidade = quantidade;
    }

    // Getters
    public Livro getLivro() {
        return livro;
    }

    public int getQuantidade() {
        return quantidade;
    }

    // Setter
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return livro.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return livro.getTitulo() + " (x" + quantidade + ") - R$" + String.format("%.2f", getSubtotal());
    }
}


