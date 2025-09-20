/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package atd1dcomersoft;

/**
 *
 * @author yasmi
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Compra {
private List<ItemCarrinho> itens;
private double valorTotal;
private LocalDateTime dataCompra;
private Cliente cliente;

public Compra(Cliente cliente) {
    this.cliente = cliente;
    this.itens = new ArrayList();
    this.dataCompra = LocalDateTime.now();
    this.valorTotal = 0.0;
}

/**
 * Adiciona um item à compra
 * @param item Item a ser adicionado
 */
public void adicionarItem(ItemCarrinho item) {
    itens.add(item);
    calcularValorTotal();
}

/**
 * Calcula o valor total da compra
 */
private void calcularValorTotal() {
    valorTotal = itens.stream()
            .mapToDouble(ItemCarrinho::getSubtotal)
            .sum();
}

/**
 * Finaliza a compra atualizando o estoque
 * @return true se a compra foi finalizada com sucesso
 */
public boolean finalizarCompra() {
    // Verifica se todos os itens têm estoque disponível
    for (ItemCarrinho item : itens) {
        if (!item.getLivro().temEstoque(item.getQuantidade())) {
            System.out.println("Estoque insuficiente para: " + item.getLivro().getTitulo());
            return false;
        }
    }

    // Remove do estoque
    for (ItemCarrinho item : itens) {
        item.getLivro().removerDoEstoque(item.getQuantidade());
    }

    // Adiciona ao histórico do cliente
    cliente.adicionarCompra(this);

    System.out.println("Compra finalizada com sucesso!");
    System.out.println("Valor total: R$ " + String.format("%.2f", valorTotal));
    return true;
}

// Getters
public List<ItemCarrinho> getItens() { return itens; }
public double getValorTotal() { return valorTotal; }
public LocalDateTime getDataCompra() { return dataCompra; }
public Cliente getCliente() { return cliente; }

public void exibirDetalhes() {
    System.out.println("=== DETALHES DA COMPRA ===");
    System.out.println("Cliente: " + cliente.getNome());
    System.out.println("Data: " + dataCompra.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    System.out.println("Itens:");
    for (ItemCarrinho item : itens) {
        System.out.printf("  - %s (Qtd: %d) - R$ %.2f%n",
                        item.getLivro().getTitulo(),
                        item.getQuantidade(),
                        item.getSubtotal());
    }
    System.out.printf("Valor Total: R$ %.2f%n", valorTotal);
}

}
