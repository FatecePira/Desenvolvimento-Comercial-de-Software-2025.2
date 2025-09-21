package com.phlu.bookstore.dao;

import com.phlu.bookstore.DatabaseConnection;
import com.phlu.bookstore.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public void adicionarLivro(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, preco, categoria, quantidade_estoque) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setDouble(3, livro.getPreco());
            stmt.setString(4, livro.getCategoria());
            stmt.setInt(5, livro.getQuantidadeEstoque());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livro> listarLivros() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livros.add(new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade_estoque")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }

    // Busca por título, autor ou categoria
    public List<Livro> buscarLivrosPorCampo(String campo, String termo) {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE LOWER(" + campo + ") LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + termo.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livros.add(new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade_estoque")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }

    public void atualizarEstoque(String titulo, int novaQuantidade) {
        String sql = "UPDATE livros SET quantidade_estoque = ? WHERE titulo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novaQuantidade);
            stmt.setString(2, titulo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Relatório: livros mais vendidos
    public List<String> livrosMaisVendidos() {
        List<String> resultados = new ArrayList<>();
        String sql = "SELECT ic.livro_id, l.titulo, SUM(ic.quantidade) AS vendas " +
                     "FROM itens_compra ic " +
                     "JOIN livros l ON ic.livro_id = l.id " +
                     "GROUP BY ic.livro_id, l.titulo " +
                     "ORDER BY vendas DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                int vendas = rs.getInt("vendas");
                resultados.add("Livro: " + titulo + " - Vendas: " + vendas);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    // Buscar livro por ID
    public Livro buscarLivroPorId(int id) {
        String sql = "SELECT * FROM livros WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade_estoque")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
