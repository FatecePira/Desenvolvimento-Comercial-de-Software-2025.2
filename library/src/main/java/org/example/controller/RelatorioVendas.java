package org.example.controller;

import org.example.model.Cliente;
import org.example.model.Compra;
import org.example.model.Livro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioVendas {
    private List<Compra> todasCompras;

    public RelatorioVendas(List<Compra> compras) {
        this.todasCompras = new ArrayList<>(compras);
    }

    // Gera relatório dos livros mais vendidos.
    public List<Map.Entry<Livro, Integer>> getLivrosMaisVendidos(int limite) {
        Map<Livro, Integer> vendasPorLivro = new HashMap<>();

        // Conta a quantidade vendida de cada livro
        for (Compra compra : todasCompras) {
            for (Map.Entry<Livro, Integer> item : compra.getItensComprados().entrySet()) {
                Livro livro = item.getKey();
                int quantidade = item.getValue();
                vendasPorLivro.put(livro, vendasPorLivro.getOrDefault(livro, 0) + quantidade);
            }
        }

        // Ordena por quantidade vendida (decrescente) e retorna os top N
        return vendasPorLivro.entrySet().stream()
                .sorted(Map.Entry.<Livro, Integer>comparingByValue().reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    //Gera relatório dos clientes que mais compraram.
    public List<Map.Entry<Cliente, Double>> getClientesQueMaisCompraram(int limite) {
        Map<Cliente, Double> gastosPorCliente = new HashMap<>();

        // Calcula o total gasto por cada cliente
        for (Compra compra : todasCompras) {
            Cliente cliente = compra.getCliente();
            double valor = compra.getValorTotal();
            gastosPorCliente.put(cliente, gastosPorCliente.getOrDefault(cliente, 0.0) + valor);
        }

        // Ordena por valor gasto (decrescente) e retorna os top N
        return gastosPorCliente.entrySet().stream()
                .sorted(Map.Entry.<Cliente, Double>comparingByValue().reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    // Calcula o faturamento total da livraria.
    public double getFaturamentoTotal() {
        return todasCompras.stream()
                .mapToDouble(Compra::getValorTotal)
                .sum();
    }

    // Calcula o total de livros vendidos.
    public int getTotalLivrosVendidos() {
        return todasCompras.stream()
                .mapToInt(Compra::getTotalLivrosComprados)
                .sum();
    }

    //Conta o número total de transações realizadas.
    public int getTotalTransacoes() {
        return todasCompras.size();
    }

    //Gera um relatório completo em formato texto.
    public String gerarRelatorioCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATORIO DE VENDAS DA LIVRARIA ===\n\n");

        // Estatísticas gerais
        sb.append("ESTATISTICAS GERAIS:\n");
        sb.append(String.format("Total de transacoes: %d\n", getTotalTransacoes()));
        sb.append(String.format("Total de livros vendidos: %d\n", getTotalLivrosVendidos()));
        sb.append(String.format("Faturamento total: R$ %.2f\n\n", getFaturamentoTotal()));

        // Top 5 livros mais vendidos
        sb.append("TOP 5 LIVROS MAIS VENDIDOS:\n");
        List<Map.Entry<Livro, Integer>> topLivros = getLivrosMaisVendidos(5);
        for (int i = 0; i < topLivros.size(); i++) {
            Map.Entry<Livro, Integer> entry = topLivros.get(i);
            sb.append(String.format("%d. %s - %d unidades vendidas\n",
                    i + 1, entry.getKey().getTitulo(), entry.getValue()));
        }

        // Top 5 clientes que mais compraram
        sb.append("\nTOP 5 CLIENTES QUE MAIS COMPRARAM (por valor):\n");
        List<Map.Entry<Cliente, Double>> topClientes = getClientesQueMaisCompraram(5);
        for (int i = 0; i < topClientes.size(); i++) {
            Map.Entry<Cliente, Double> entry = topClientes.get(i);
            sb.append(String.format("%d. %s - R$ %.2f\n",
                    i + 1, entry.getKey().getNome(), entry.getValue()));
        }

        return sb.toString();
    }

    // Adiciona uma nova compra à lista para atualizar os relatórios.
    public void adicionarCompra(Compra compra) {
        if (compra != null) {
            this.todasCompras.add(compra);
        }
    }
}
