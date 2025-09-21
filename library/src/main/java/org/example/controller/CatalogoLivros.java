package org.example.controller;

import org.example.model.Livro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogoLivros {
    private List<Livro> livros;

    public CatalogoLivros() {
        this.livros = new ArrayList<>();
    }

    // add livro
    public void adicionarLivro(Livro livro) {
        if (livro != null) {
            livros.add(livro);
        }
    }

    //busca por titulo exato
    public Livro buscarPorTitulo(String titulo) {
        return livros.stream()
                .filter(livro -> livro.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    // busca por titulo contendo
    public List<Livro> buscarPorTituloContendo(String titulo) {
        return livros.stream()
                .filter(livro -> livro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    // busca livro por autor
    public List<Livro> buscarPorAutor(String autor) {
        return livros.stream()
                .filter(livro -> livro.getAutor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }

    // busca livro por nome da categoria
    public List<Livro> buscarPorCategoria(String nomeCategoria) {
        return livros.stream()
                .filter(livro -> livro.getCategoria().getNome().equalsIgnoreCase(nomeCategoria))
                .collect(Collectors.toList());
    }

    // total de livros no catálogo
    public int getTotalLivros() {
        return livros.size();
    }

    // verifica se o catálogo está vazio
    public boolean isEmpty() {
        return livros.isEmpty();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Catálogo vazio";
        }

        StringBuilder sb = new StringBuilder("Catálogo de Livros:\n");
        for (Livro livro : livros) {
            sb.append(String.format("- %s por %s (Categoria: %s) - R$ %.2f - Estoque: %d\n",
                    livro.getTitulo(), livro.getAutor(), livro.getCategoria().getNome(),
                    livro.getPreco(), livro.getQtdeEstoque()));
        }
        return sb.toString();
    }
}
