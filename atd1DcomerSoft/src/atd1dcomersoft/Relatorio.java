package atd1dcomersoft;

import java.util.*;

class Relatorio {
private CatalogoLivros catalogo;
private List<Cliente> clientes;

public Relatorio(CatalogoLivros catalogo, List<Cliente> clientes) {
    this.catalogo = catalogo;
    this.clientes = clientes;
}

/**
 * Gera relatório dos livros mais vendidos
 */
public void livrosMaisVendidos() {
    System.out.println("=== RELATÓRIO: LIVROS MAIS VENDIDOS ===");

    List<Livro> livrosOrdenados = catalogo.getLivros().stream()
            .filter(livro -> livro.getQuantidadeVendida() > 0)
            .sorted((l1, l2) -> Integer.compare(l2.getQuantidadeVendida(), l1.getQuantidadeVendida()))
            .toList();

    if (livrosOrdenados.isEmpty()) {
        System.out.println("Nenhuma venda registrada ainda.");
    } else {
        for (int i = 0; i < Math.min(10, livrosOrdenados.size()); i++) {
            Livro livro = livrosOrdenados.get(i);
            System.out.printf("%d. %s - %d unidades vendidas%n",
                            i + 1, livro.getTitulo(), livro.getQuantidadeVendida());
        }
    }
    System.out.println();
}

/**
 * Gera relatório dos clientes que mais compraram
 */
public void clientesMaisCompraram() {
    System.out.println("=== RELATÓRIO: CLIENTES QUE MAIS COMPRARAM ===");

    List<Cliente> clientesOrdenados = clientes.stream()
            .filter(cliente -> !cliente.getHistoricoCompra().isEmpty())
            .sorted((c1, c2) -> Double.compare(c2.getTotalGasto(), c1.getTotalGasto()))
            .toList();

    if (clientesOrdenados.isEmpty()) {
        System.out.println("Nenhuma compra registrada ainda.");
    } else {
        for (int i = 0; i < Math.min(10, clientesOrdenados.size()); i++) {
            Cliente cliente = clientesOrdenados.get(i);
            System.out.printf("%d. %s - R$ %.2f em %d compras%n",
                            i + 1,
                            cliente.getNome(),
                            cliente.getTotalGasto(),
                            cliente.getHistoricoCompra().size());
        }
    }
    System.out.println();
}
}


