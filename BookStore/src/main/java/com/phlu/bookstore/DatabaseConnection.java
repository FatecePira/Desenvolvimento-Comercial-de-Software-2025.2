package com.phlu.bookstore;

import java.sql.*;

public class DatabaseConnection {
    // Banco persistente em arquivo (fica na pasta ./database)
    private static final String URL = "jdbc:h2:./database/bookstoredb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            criarTabelasSeNaoExistirem(connection);
        }
        return connection;
    }

    private static void criarTabelasSeNaoExistirem(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // usuarios
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS usuarios (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  nome  VARCHAR(255),
                  email VARCHAR(255) UNIQUE,
                  senha VARCHAR(255),
                  tipo  VARCHAR(50),
                  cpf   VARCHAR(50)
                )
            """);

            // livros
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS livros (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  titulo VARCHAR(255),
                  autor VARCHAR(255),
                  preco DOUBLE,
                  categoria VARCHAR(100),
                  quantidade_estoque INT
                )
            """);

            // compras
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS compras (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  usuario_id INT NOT NULL,
                  data_compra DATE NOT NULL,
                  valor_total DOUBLE NOT NULL,
                  FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )
            """);

            // itens_compra
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS itens_compra (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  compra_id INT NOT NULL,
                  livro_id  INT NOT NULL,
                  quantidade INT NOT NULL,
                  FOREIGN KEY (compra_id) REFERENCES compras(id) ON DELETE CASCADE,
                  FOREIGN KEY (livro_id)  REFERENCES livros(id)
                )
            """);

            // ===== Carrinho persistente =====
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS carrinhos (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  usuario_id INT NOT NULL UNIQUE,
                  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                  FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS itens_carrinho (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  carrinho_id INT NOT NULL,
                  livro_id    INT NOT NULL,
                  quantidade  INT NOT NULL,
                  UNIQUE(carrinho_id, livro_id),
                  FOREIGN KEY (carrinho_id) REFERENCES carrinhos(id) ON DELETE CASCADE,
                  FOREIGN KEY (livro_id)    REFERENCES livros(id)
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
