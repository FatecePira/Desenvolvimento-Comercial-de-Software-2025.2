/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package atd1dcomersoft;

/**
 *
 * @author yasmi
 */

class Administrador extends Usuario {

public Administrador(String nome, String email, String cpf) {
    super(nome, email, cpf);
}

/**
 * Administrador pode gerenciar o catálogo de livros
 * @param catalogo Catálogo a ser gerenciado
 * @param livro Livro a ser adicionado
 */
public void adicionarLivroAoCatalogo(CatalogoLivros catalogo, Livro livro) {
    catalogo.adicionarLivro(livro);
    System.out.println("Livro '" + livro.getTitulo() + "' adicionado ao catálogo por " + nome);
}

@Override
public void exibirInformacoes() {
    System.out.println("=== INFORMAÇÕES DO ADMINISTRADOR ===");
    System.out.println("Nome: " + nome);
    System.out.println("E-mail: " + email);
    System.out.println("CPF: " + cpf);
    System.out.println("Função: Administrador do Sistema");
}
}