package org.example.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Compra {
    private static int proximoId = 1;

    private int id;
    private Cliente cliente;
    private Map<Livro, Integer> itensComprados;
    private double valorTotal;
    private LocalDateTime dataCompra;

    public Compra(Cliente cliente, Map<Livro, Integer> itensComprados) {
        this.id = proximoId++;
        this.cliente = cliente;
        this.itensComprados = new HashMap<>(itensComprados);
        this.dataCompra = LocalDateTime.now();
        this.valorTotal = calcularValorTotal();
    }

    private double calcularValorTotal() {
        return itensComprados.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPreco() * entry.getValue())
                .sum();
    }

    // Processa a compra, atualizando o estoque dos livros
    public boolean processarCompra() {
        // Verifica se há estoque suficiente para todos os itens
        for (Map.Entry<Livro, Integer> entry : itensComprados.entrySet()) {
            Livro livro = entry.getKey();
            int quantidade = entry.getValue();

            if (livro.getQtdeEstoque() < quantidade) {
                System.out.println("Estoque insuficiente para o livro: " + livro.getTitulo());
                return false;
            }
        }

        // Atualiza o estoque dos livros
        for (Map.Entry<Livro, Integer> entry : itensComprados.entrySet()) {
            Livro livro = entry.getKey();
            int quantidade = entry.getValue();
            livro.setQtdeEstoque(livro.getQtdeEstoque() - quantidade);
        }

        return true;
    }

    // Numero total de livros comprados
    public int getTotalLivrosComprados() {
        return itensComprados.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Getters
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Map<Livro, Integer> getItensComprados() {
        return new HashMap<>(itensComprados);
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Compra #%d - Cliente: %s\n", id, cliente.getNome()));
        sb.append(String.format("Data: %s\n", dataCompra.toString()));
        sb.append("Itens:\n");

        for (Map.Entry<Livro, Integer> entry : itensComprados.entrySet()) {
            Livro livro = entry.getKey();
            int quantidade = entry.getValue();
            double subtotal = livro.getPreco() * quantidade;
            sb.append(String.format("- %s (x%d) - R$ %.2f\n",
                    livro.getTitulo(), quantidade, subtotal));
        }

        sb.append(String.format("Valor Total: R$ %.2f", valorTotal));
        return sb.toString();
    }
}
