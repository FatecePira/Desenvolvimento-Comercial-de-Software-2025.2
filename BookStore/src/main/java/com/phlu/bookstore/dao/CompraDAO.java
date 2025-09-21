package com.phlu.bookstore.dao;

import com.phlu.bookstore.DatabaseConnection;
import com.phlu.bookstore.model.Compra;
import com.phlu.bookstore.model.Livro;

import java.sql.*;
import java.util.*;

public class CompraDAO {

    /** Compatibilidade: evita quebrar chamadas antigas, mas prefira salvarCompraComIds(...) */
    @Deprecated
    public void salvarCompra(int usuarioId, Compra compra) {
        Map<Livro,Integer> itens = compra.getItens();
        List<Integer> ids = new ArrayList<>(itens.size());
        try (Connection c = DatabaseConnection.getConnection()) {
            for (Livro l : itens.keySet()) {
                Integer id = buscarLivroIdPorTituloAutor(c, l.getTitulo(), l.getAutor());
                if (id == null) id = buscarLivroIdPorTitulo(c, l.getTitulo());
                if (id == null) id = 0; // pode estourar FK — por isso prefira a nova API
                ids.add(id);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        salvarCompraComIds(usuarioId, compra, ids);
    }

    /** Nova API: salva compra com IDs dos livros alinhados à ordem dos itens */
    public int salvarCompraComIds(int usuarioId, Compra compra, List<Integer> livroIdsEmOrdem) {
        String sqlCompra = "INSERT INTO compras (usuario_id, data_compra, valor_total) VALUES (?, ?, ?)";
        String sqlItem   = "INSERT INTO itens_compra (compra_id, livro_id, quantidade) VALUES (?, ?, ?)";

        List<Map.Entry<Livro,Integer>> entradas = new ArrayList<>(compra.getItens().entrySet());
        if (livroIdsEmOrdem == null || livroIdsEmOrdem.size() != entradas.size()) {
            throw new IllegalArgumentException("IDs de livros não correspondem ao número de itens da compra.");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            boolean prev = conn.getAutoCommit();
            conn.setAutoCommit(false);
            int compraId = -1;

            try (PreparedStatement psC = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                java.sql.Date data = (compra.getDataCompra() != null)
                        ? java.sql.Date.valueOf(compra.getDataCompra())
                        : new java.sql.Date(System.currentTimeMillis());
                psC.setInt(1, usuarioId);
                psC.setDate(2, data);
                psC.setDouble(3, compra.getTotal());
                psC.executeUpdate();
                try (ResultSet rk = psC.getGeneratedKeys()) {
                    if (rk.next()) compraId = rk.getInt(1);
                }
            }

            try (PreparedStatement psI = conn.prepareStatement(sqlItem)) {
                for (int i = 0; i < entradas.size(); i++) {
                    int livroId = livroIdsEmOrdem.get(i);
                    int qtd     = entradas.get(i).getValue();
                    psI.setInt(1, compraId);
                    psI.setInt(2, livroId);
                    psI.setInt(3, qtd);
                    psI.addBatch();
                }
                psI.executeBatch();
            }

            conn.commit();
            conn.setAutoCommit(prev);
            return compraId;

        } catch (SQLException e) {
            e.printStackTrace();
            try { DatabaseConnection.getConnection().rollback(); } catch (Exception ignore) {}
            try { DatabaseConnection.getConnection().setAutoCommit(true); } catch (Exception ignore) {}
            return -1;
        }
    }

    /** Histórico de compras do usuário (lendo do banco) */
    public List<Compra> listarComprasDoUsuario(int usuarioId) {
        List<Compra> out = new ArrayList<>();

        String qCompras = "SELECT id FROM compras WHERE usuario_id=? ORDER BY id DESC";
        String qItens   = """
            SELECT ic.livro_id, ic.quantidade,
                   l.titulo, l.autor, l.preco, l.categoria, l.quantidade_estoque
            FROM itens_compra ic
            JOIN livros l ON l.id = ic.livro_id
            WHERE ic.compra_id = ?
            ORDER BY l.id
        """;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement psC = c.prepareStatement(qCompras)) {

            psC.setInt(1, usuarioId);

            try (ResultSet rsC = psC.executeQuery()) {
                while (rsC.next()) {
                    int compraId = rsC.getInt("id");

                    LinkedHashMap<Livro, Integer> itens = new LinkedHashMap<>();
                    try (PreparedStatement psI = c.prepareStatement(qItens)) {
                        psI.setInt(1, compraId);
                        try (ResultSet rsI = psI.executeQuery()) {
                            while (rsI.next()) {
                                Livro l = new Livro(
                                        rsI.getString("titulo"),
                                        rsI.getString("autor"),
                                        rsI.getDouble("preco"),
                                        rsI.getString("categoria"),
                                        rsI.getInt("quantidade_estoque")
                                );
                                itens.put(l, rsI.getInt("quantidade"));
                            }
                        }
                    }

                    out.add(new Compra(itens));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    // Helpers (usados pela API antiga)
    private Integer buscarLivroIdPorTituloAutor(Connection c, String titulo, String autor) throws SQLException {
        String q = "SELECT id FROM livros WHERE titulo=? AND autor=? ORDER BY id LIMIT 1";
        try (PreparedStatement ps = c.prepareStatement(q)) {
            ps.setString(1, titulo);
            ps.setString(2, autor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }

    private Integer buscarLivroIdPorTitulo(Connection c, String titulo) throws SQLException {
        String q = "SELECT id FROM livros WHERE titulo=? ORDER BY id LIMIT 1";
        try (PreparedStatement ps = c.prepareStatement(q)) {
            ps.setString(1, titulo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }
}
