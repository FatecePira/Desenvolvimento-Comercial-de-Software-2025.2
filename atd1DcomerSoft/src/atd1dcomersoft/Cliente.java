/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package atd1dcomersoft;
import java.util.*;

/**
 *
 * @author yasmi
 */

class Cliente extends Usuario {
private final List<Compra> historicoCompra;

/**
 * Construtor da classe Cliente
 * @param nome Nome do cliente
 * @param email E-mail do cliente
 * @param cpf CPF do cliente
 */
public Cliente(String nome, String email, String cpf) {
    super(nome, email, cpf);
    this.historicoCompra = new ArrayList<>();
}

/**
 * Adiciona uma compra ao histórico do cliente
 * @param compra Objeto Compra a ser adicionado
 */
public void adicionarCompra(Compra compra) {
    historicoCompra.add(compra);
}

/**
 * Calcula o total gasto pelo cliente em todas as compras
 * @return Valor total gasto
 */
public double getTotalGasto() {
    return historicoCompra.stream()
            .mapToDouble(Compra::getValorTotal)
            .sum();
}

/**
 * Retorna a quantidade total de livros comprados pelo cliente
 * @return Quantidade de livros comprados
 */
public int getQuantidadeLivrosComprados() {
    return historicoCompra.stream()
            .mapToInt(compra -> compra.getItens().size())
            .sum();
}

public List<Compra> getHistoricoCompra() { return historicoCompra; }

@Override
public void exibirInformacoes() {
    System.out.println("=== INFORMAÇÕES DO CLIENTE ===");
    System.out.println("Nome: " + nome);
    System.out.println("E-mail: " + email);
    System.out.println("CPF: " + cpf);
    System.out.println("Total gasto: R$ " + String.format("%.2f", getTotalGasto()));
    System.out.println("Compras realizadas: " + historicoCompra.size());
}

@Override
    public String getNome() {
       return this.nome;
    }
}