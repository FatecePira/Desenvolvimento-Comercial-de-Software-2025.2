/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package atd1dcomersoft;

/**
 *
 * @author yasmi
 */
class ItemCarrinho {
private Livro livro;
private int quantidade;

public ItemCarrinho(Livro livro, int quantidade) {
    this.livro = livro;
    this.quantidade = quantidade;
}

/**
 * Calcula o subtotal do item (preço × quantidade)
 * @return Valor do subtotal
 */
public double getSubtotal() {
    return livro.getPreco() * quantidade;
}

// Getters e Setters
public Livro getLivro() { return livro; }
public int getQuantidade() { return quantidade; }
public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

}