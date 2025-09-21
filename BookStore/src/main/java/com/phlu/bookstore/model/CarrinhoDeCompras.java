package com.phlu.bookstore.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CarrinhoDeCompras {

    private Map<Livro, Integer> itens;

    public CarrinhoDeCompras() { 
        this.itens = new HashMap<>(); 
    }

    // Adiciona um livro ao carrinho
    public void adicionarLivro(Livro livro) {
        itens.put(livro, itens.getOrDefault(livro, 0) + 1);
    }

    // Remove um livro do carrinho (uma unidade por vez)
    public void removerLivro(Livro livro) {
        if (!itens.containsKey(livro)) return;
        int qtd = itens.get(livro);
        if (qtd > 1) itens.put(livro, qtd - 1);
        else itens.remove(livro);
    }

    // Calcula o total do carrinho
    public double calcularTotal() {
        return itens.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPreco() * e.getValue())
                .sum();
    }

    // Verifica se está vazio
    public boolean estaVazio() { 
        return itens.isEmpty(); 
    }

    // Retorna os itens como Set (para exibição)
    public Set<Map.Entry<Livro, Integer>> getItens() { 
        return itens.entrySet(); 
    }

    public Map<Livro, Integer> getItensMap() {
        return itens;
    }

    // Limpa o carrinho
    public void limparCarrinho() { 
        itens.clear(); 
    }
}
