package com.phlu.bookstore.model;

import java.time.LocalDate;
import java.util.Map;

public class Compra {

    private Map<Livro, Integer> itens;
    private double total;
    private LocalDate dataCompra;

    public Compra(Map<Livro, Integer> itens) {
        this.itens = itens;
        this.total = calcularTotal();
        this.dataCompra = LocalDate.now();
    }

    private double calcularTotal() {
        return itens.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPreco() * e.getValue())
                .sum();
    }

    public Map<Livro, Integer> getItens() { return itens; }
    public double getTotal() { return total; }
    public LocalDate getDataCompra() { return dataCompra; }

    public LocalDate getDataEntregaPrevista() {
        return dataCompra.plusDays(5 + (int)(Math.random() * 26)); // entrega 5 a 30 dias
    }
}
