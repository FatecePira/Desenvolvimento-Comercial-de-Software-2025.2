package com.livraria.usuarios;

import com.livraria.vendas.Pedido;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um cliente, que é um tipo de Usuário.
 * A principal diferença é que o cliente tem um histórico de compras.
 */

public class Cliente extends Usuario {
    private List<Pedido> historicoDeCompras;

    public Cliente(String nome, String email, String cpf) {
        super(nome, email, cpf);
        this.historicoDeCompras = new ArrayList<>();
    }

    public List<Pedido> getHistoricoDeCompras() {
        return historicoDeCompras;
    }

    public void adicionarCompraAoHistorico(Pedido pedido) {
        this.historicoDeCompras.add(pedido);
    }

    @Override
    public String toString() {
        return "Cliente - " + super.toString() + ", Compras: " + historicoDeCompras.size();
    }
}


