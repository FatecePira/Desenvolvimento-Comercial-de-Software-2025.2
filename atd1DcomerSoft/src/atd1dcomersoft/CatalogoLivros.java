
package atd1dcomersoft;

import java.util.*;


class CatalogoLivros {
private List<Livro> livros;

public CatalogoLivros() {
    this.livros = new ArrayList<>();
}

/**
 * Adiciona um livro ao catálogo
 * @param livro Livro a ser adicionado
 */
public void adicionarLivro(Livro livro) {
    livros.add(livro);
}

/**
 * Busca livros por título
 * @param titulo Título a ser buscado
 * @return Lista de livros encontrados
 */
public List<Livro> buscarPorTitulo(String titulo) {
    return livros.stream()
            .filter(livro -> livro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            .toList();
}

/**
 * Busca livros por autor
 * @param autor Autor a ser buscado
 * @return Lista de livros encontrados
 */
public List<Livro> buscarPorAutor(String autor) {
    return livros.stream()
            .filter(livro -> livro.getAutor().toLowerCase().contains(autor.toLowerCase()))
            .toList();
}

/**
 * Busca livros por categoria
 * @param categoria Categoria a ser buscada
 * @return Lista de livros encontrados
 */
public List<Livro> buscarPorCategoria(String categoria) {
    return livros.stream()
            .filter(livro -> livro.getCategoria().toLowerCase().contains(categoria.toLowerCase()))
            .toList();
}

/**
 * Método genérico de busca
 * @param termo Termo de busca
 * @param tipo Tipo de busca ("titulo", "autor", "categoria")
 * @return Lista de livros encontrados
 */
public List<Livro> buscarLivros(String termo, String tipo) {
    return switch (tipo.toLowerCase()) {
        case "titulo" -> buscarPorTitulo(termo);
        case "autor" -> buscarPorAutor(termo);
        case "categoria" -> buscarPorCategoria(termo);
        default -> new ArrayList<>();
    };
}

/**
 * Exibe todos os livros do catálogo
 */
public void exibirCatalogo() {
    System.out.println("=== CATÁLOGO DE LIVROS ===");
    if (livros.isEmpty()) {
        System.out.println("Nenhum livro cadastrado.");
    } else {
        for (int i = 0; i < livros.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, livros.get(i));
        }
    }
}

public List<Livro> getLivros() { return livros; }
public Livro getLivro(int indice) {
    if (indice >= 0 && indice < livros.size()) {
        return livros.get(indice);
    }
    return null;
}

}
