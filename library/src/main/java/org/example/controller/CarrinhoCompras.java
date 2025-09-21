package org.example.controller;

import org.example.model.Livro;

import java.util.HashMap;
import java.util.Map;

public class CarrinhoCompras {
    private Map<Livro, Integer> itens;

    public CarrinhoCompras() {
        this.itens = new HashMap<>();
    }

    public boolean adicionarItem(Livro livro, int quantidade) {
        if (livro == null || quantidade <= 0) {
            return false;
        }
        // Verifica se há estoque suficiente
        int quantidadeAtual = itens.getOrDefault(livro, 0);
        if (quantidadeAtual + quantidade > livro.getQtdeEstoque()) {
            System.out.println("Estoque insuficiente para o livro: " + livro.getTitulo());
            return false;
        }
        itens.put(livro, quantidadeAtual + quantidade);
        return true;
    }

    // Remove items do carrinho
    public boolean removerItem(Livro livro, int quantidade) {
        if (livro == null || quantidade <= 0 || !itens.containsKey(livro)) {
            return false;
        }

        int quantidadeAtual = itens.get(livro);
        if (quantidade >= quantidadeAtual) {
            itens.remove(livro);
        } else {
            itens.put(livro, quantidadeAtual - quantidade);
        }

        return true;
    }

    // Calcula o total do carrinho
    public double calcularTotal() {
        return itens.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPreco() * entry.getValue())
                .sum();
    }

    // Verifica se o carrinho está vazio
    public boolean isEmpty() {
        return itens.isEmpty();
    }

    // Limpa o carrinho
    public void limpar() {
        itens.clear();
    }

    // Getters
    public Map<Livro, Integer> getItens() {
        return new HashMap<>(itens);
    }


    @Override
    public String toString() {
        if (isEmpty()) {
            return "Carrinho vazio";
        }

        StringBuilder sb = new StringBuilder("Carrinho de Compras:\n");
        for (Map.Entry<Livro, Integer> entry : itens.entrySet()) {
            Livro livro = entry.getKey();
            int quantidade = entry.getValue();
            double subtotal = livro.getPreco() * quantidade;
            sb.append(String.format("- %s (x%d) - R$ %.2f\n",
                    livro.getTitulo(), quantidade, subtotal));
        }
        sb.append(String.format("Total: R$ %.2f", calcularTotal()));
        return sb.toString();
    }
}
