/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package atd1dcomersoft;

/**
 *
 * @author yasmi
 */
class Livro {
private String titulo;
private String autor;
private double preco;
private String categoria;
private int quantidadeEstoque;
private int quantidadeVendida; // Para relatórios

/**
 * Construtor da classe Livro
 */
public Livro(String titulo, String autor, double preco, String categoria, int quantidadeEstoque) {
    this.titulo = titulo;
    this.autor = autor;
    this.preco = preco;
    this.categoria = categoria;
    this.quantidadeEstoque = quantidadeEstoque;
    this.quantidadeVendida = 0;
}

/**
 * Verifica se há estoque disponível
 * @param quantidade Quantidade desejada
 * @return true se há estoque suficiente
 */
public boolean temEstoque(int quantidade) {
    return quantidadeEstoque >= quantidade;
}

/**
 * Remove quantidade do estoque (usado na finalização da compra)
 * @param quantidade Quantidade a ser removida
 * @return true se a operação foi bem-sucedida
 */
public boolean removerDoEstoque(int quantidade) {
    if (temEstoque(quantidade)) {
        quantidadeEstoque -= quantidade;
        quantidadeVendida += quantidade;
        return true;
    }
    return false;
}

// Getters e Setters
public String getTitulo() { return titulo; }
public void setTitulo(String titulo) { this.titulo = titulo; }

public String getAutor() { return autor; }
public void setAutor(String autor) { this.autor = autor; }

public double getPreco() { return preco; }
public void setPreco(double preco) { this.preco = preco; }

public String getCategoria() { return categoria; }
public void setCategoria(String categoria) { this.categoria = categoria; }

public int getQuantidadeEstoque() { return quantidadeEstoque; }
public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

public int getQuantidadeVendida() { return quantidadeVendida; }

@Override
public String toString() {
    return String.format("'%s' por %s - R$ %.2f (Estoque: %d)",
                       titulo, autor, preco, quantidadeEstoque);
}

    Object getHistoricoCompra() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void adicionarLivroAoCatalogo(CatalogoLivros catalogo, Livro novoLivro) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
