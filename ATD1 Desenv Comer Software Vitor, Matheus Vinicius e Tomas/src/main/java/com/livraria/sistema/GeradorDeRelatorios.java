package com.livraria.sistema;

import com.livraria.catalogo.Livro;
import com.livraria.usuarios.Cliente;
import com.livraria.vendas.ItemCarrinho;
import com.livraria.vendas.Pedido;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe com métodos estáticos só pra gerar relatórios.
 * Não precisa criar um objeto dela, é só chamar os métodos direto.
 */


public class GeradorDeRelatorios {

    public static void gerarRelatorioLivrosMaisVendidos(List<Pedido> pedidos) {
        System.out.println("\n--- Relatório de Livros Mais Vendidos ---");
        Map<Livro, Integer> vendasPorLivro = new HashMap<>();

        for (Pedido pedido : pedidos) {
            for (ItemCarrinho item : pedido.getItensComprados()) {
                vendasPorLivro.merge(item.getLivro(), item.getQuantidade(), Integer::sum);
            }
        }

        vendasPorLivro.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println("\"" + entry.getKey().getTitulo() + "\" - " + entry.getValue() + " unidades"));
        System.out.println("----------------------------------------\n");
    }

    public static void gerarRelatorioClientesQueMaisCompraram(List<Pedido> pedidos) {
        System.out.println("\n--- Relatório de Clientes que Mais Compraram ---");
        Map<Cliente, Double> gastosPorCliente = new HashMap<>();

        for (Pedido pedido : pedidos) {
            gastosPorCliente.merge(pedido.getCliente(), pedido.getValorTotal(), Double::sum);
        }

        gastosPorCliente.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(entry.getKey().getNome() + " (CPF: " + entry.getKey().getCpf() + ") - R$" + String.format("%.2f", entry.getValue())));
        System.out.println("----------------------------------------------\n");
    }
}


