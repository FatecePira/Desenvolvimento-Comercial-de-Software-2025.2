package com.phlu.bookstore.dao;

import com.phlu.bookstore.DatabaseConnection;
import com.phlu.bookstore.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CarrinhoDAO {

    // Garante o carrinho do usuário e retorna seu ID
    private int garantirCarrinho(int usuarioId) throws SQLException {
        String q = "SELECT id FROM carrinhos WHERE usuario_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(q)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        String ins = "INSERT INTO carrinhos (usuario_id) VALUES (?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // MERGE sem a coluna 'id' (evita NULL em PK auto-increment)
    public void adicionarItem(int usuarioId, int livroId, int quantidade) {
        try {
            int carrinhoId = garantirCarrinho(usuarioId);
            String upsert = """
                MERGE INTO itens_carrinho (carrinho_id, livro_id, quantidade)
                KEY(carrinho_id, livro_id)
                VALUES (
                  ?, ?, COALESCE((SELECT quantidade FROM itens_carrinho WHERE carrinho_id=? AND livro_id=?),0) + ?
                )
            """;
            try (Connection c = DatabaseConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(upsert)) {
                ps.setInt(1, carrinhoId);
                ps.setInt(2, livroId);
                ps.setInt(3, carrinhoId);
                ps.setInt(4, livroId);
                ps.setInt(5, quantidade);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarQuantidade(int usuarioId, int livroId, int novaQtd) {
        String sql = """
            UPDATE itens_carrinho SET quantidade=?
            WHERE carrinho_id=(SELECT id FROM carrinhos WHERE usuario_id=?) AND livro_id=?
        """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Math.max(0, novaQtd));
            ps.setInt(2, usuarioId);
            ps.setInt(3, livroId);
            ps.executeUpdate();

            try (PreparedStatement del = c.prepareStatement(
                "DELETE FROM itens_carrinho WHERE quantidade<=0 AND carrinho_id=(SELECT id FROM carrinhos WHERE usuario_id=?)")) {
                del.setInt(1, usuarioId);
                del.executeUpdate();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void removerItem(int usuarioId, int livroId) {
        String sql = "DELETE FROM itens_carrinho WHERE carrinho_id=(SELECT id FROM carrinhos WHERE usuario_id=?) AND livro_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, livroId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void limpar(int usuarioId) {
        String sql = "DELETE FROM itens_carrinho WHERE carrinho_id=(SELECT id FROM carrinhos WHERE usuario_id=?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** Itens do carrinho (Livro + quantidade) em ordem por id do livro */
    public Map<Livro, Integer> listarItens(int usuarioId) {
        Map<Livro, Integer> itens = new LinkedHashMap<>();
        String sql = """
            SELECT l.id AS livro_id, ic.quantidade, l.titulo, l.autor, l.preco, l.categoria, l.quantidade_estoque
            FROM itens_carrinho ic
            JOIN carrinhos c ON c.id = ic.carrinho_id
            JOIN livros    l ON l.id = ic.livro_id
            WHERE c.usuario_id=?
            ORDER BY l.id
        """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Livro l = new Livro(
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade_estoque")
                    );
                    itens.put(l, rs.getInt("quantidade"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return itens;
    }

    /** IDs dos livros do carrinho na MESMA ordem do listarItens() */
    public List<Integer> listarLivroIdsNoCarrinho(int usuarioId) {
        List<Integer> ids = new ArrayList<>();
        String sql = """
            SELECT l.id
            FROM itens_carrinho ic
            JOIN carrinhos c ON c.id = ic.carrinho_id
            JOIN livros    l ON l.id = ic.livro_id
            WHERE c.usuario_id=?
            ORDER BY l.id
        """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt(1));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ids;
    }
}
