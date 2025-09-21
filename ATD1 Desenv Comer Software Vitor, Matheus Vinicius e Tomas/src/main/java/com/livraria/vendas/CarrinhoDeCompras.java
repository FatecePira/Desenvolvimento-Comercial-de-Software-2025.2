package com.livraria.vendas;

import com.livraria.catalogo.Livro;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoDeCompras {
    private List<ItemCarrinho> itens;

    public CarrinhoDeCompras() {
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(Livro livro, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (livro.getEstoque() < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para o livro: " + livro.getTitulo());
        }

        for (ItemCarrinho item : itens) {
            if (item.getLivro().equals(livro)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        itens.add(new ItemCarrinho(livro, quantidade));
    }

    public void removerItem(Livro livro) {
        itens.removeIf(item -> item.getLivro().equals(livro));
    }

    public double calcularTotal() {
        return itens.stream()
                .mapToDouble(item -> item.getLivro().getPreco() * item.getQuantidade())
                .sum();
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void limparCarrinho() {
        this.itens.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n--- Carrinho de Compras ---\n");
        if (itens.isEmpty()) {
            sb.append("Carrinho vazio.\n");
        } else {
            for (ItemCarrinho item : itens) {
                sb.append(item).append("\n");
            }
            sb.append("Total: R$").append(String.format("%.2f", calcularTotal())).append("\n");
        }
        sb.append("---------------------------\n");
        return sb.toString();
    }
}


