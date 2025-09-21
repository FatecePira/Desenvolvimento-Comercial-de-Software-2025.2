package com.phlu.bookstore.dao;

import com.phlu.bookstore.DatabaseConnection;
import com.phlu.bookstore.model.Administrador;
import com.phlu.bookstore.model.Cliente;
import com.phlu.bookstore.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void adicionarUsuario(Usuario usuario, String tipo) {
        String sql = "INSERT INTO usuarios (nome, email, senha, tipo, cpf) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha()); // senha já hashada
            stmt.setString(4, tipo);
            if (usuario instanceof Cliente) {
                stmt.setString(5, ((Cliente) usuario).getCpf());
            } else {
                stmt.setString(5, null); // admin não tem CPF
            }
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id       = rs.getInt("id");
                    String nome  = rs.getString("nome");
                    String senha = rs.getString("senha");   // já vem HASH no banco
                    String tipo  = rs.getString("tipo");
                    String cpf   = rs.getString("cpf");

                    if ("administrador".equalsIgnoreCase(tipo)) {
                        // Administrador NÃO recebe id no construtor
                        // 'true' porque a senha já está hasheada no banco
                        return new Administrador(nome, email, senha, /*hashed=*/true);
                    } else {
                        // Cliente recebe id como primeiro argumento
                        return new Cliente(id, nome, email, senha, cpf, /*hashed=*/true);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existeAdministrador() {
        String sql = "SELECT COUNT(*) AS total FROM usuarios WHERE tipo = 'administrador'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("total") > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<String> clientesMaisCompraram() {
        List<String> resultados = new ArrayList<>();
        String sql = "SELECT usuario_id, COUNT(*) AS total_compras FROM compras GROUP BY usuario_id ORDER BY total_compras DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultados.add("Cliente ID: " + rs.getInt("usuario_id") + " - Compras: " + rs.getInt("total_compras"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return resultados;
    }
}
