package org.example.model;

import org.example.controller.CarrinhoCompras;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private List<Compra> historicoCompras;
    private CarrinhoCompras carrinho;

    public Cliente(String nome, String email, String cpf) {
        super(nome, email, cpf);
        this.setTipoUsuario(TipoUsuario.CLIENTE);
        this.historicoCompras = new ArrayList<>();
        this.carrinho = new CarrinhoCompras();
    }

    public void adicionarCompraAoHistorico(Compra compra) {
        this.historicoCompras.add(compra);
    }

    public double calcularTotalGasto() {
        return historicoCompras.stream()
                .mapToDouble(Compra::getValorTotal)
                .sum();
    }

    public int getNumeroDeCompras() {
        return historicoCompras.size();
    }

    public List<Compra> getHistoricoCompras() {
        return new ArrayList<>(historicoCompras);
    }

    public CarrinhoCompras getCarrinho() {
        return carrinho;
    }

    @Override
    public String toString() {
        return String.format("Cliente: %s (%s) - CPF: %s- [HISTÓRICO DE COMPRAS] Total gasto: R$ %.2f - Compras: %d",
                getNome(), getEmail(), getCpf(), calcularTotalGasto(), getNumeroDeCompras());
    }
}
