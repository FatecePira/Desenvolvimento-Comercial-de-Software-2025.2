package com.livraria.vendas;

import com.livraria.usuarios.Cliente;
import java.time.LocalDateTime;
import java.util.List;

public class Pedido {
    private String idPedido;
    private Cliente cliente;
    private List<ItemCarrinho> itensComprados;
    private double valorTotal;
    private LocalDateTime dataPedido;

    public Pedido(String idPedido, Cliente cliente, List<ItemCarrinho> itensComprados, double valorTotal) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.itensComprados = itensComprados;
        this.valorTotal = valorTotal;
        this.dataPedido = LocalDateTime.now();
    }

    // Getters
    public String getIdPedido() {
        return idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemCarrinho> getItensComprados() {
        return itensComprados;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n--- Detalhes do Pedido ---\n");
        sb.append("ID do Pedido: ").append(idPedido).append("\n");
        sb.append("Cliente: ").append(cliente.getNome()).append(" (CPF: ").append(cliente.getCpf()).append(")\n");
        sb.append("Data do Pedido: ").append(dataPedido).append("\n");
        sb.append("Itens:\n");
        for (ItemCarrinho item : itensComprados) {
            sb.append("  - ").append(item.getLivro().getTitulo()).append(" (x").append(item.getQuantidade()).append(") - R$").append(String.format("%.2f", item.getSubtotal())).append("\n");
        }
        sb.append("Valor Total: R$").append(String.format("%.2f", valorTotal)).append("\n");
        sb.append("---------------------------\n");
        return sb.toString();
    }
}


