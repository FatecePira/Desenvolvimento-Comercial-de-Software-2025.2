package com.phlu.bookstore.service;

import com.phlu.bookstore.dao.UsuarioDAO;
import com.phlu.bookstore.model.Administrador;
import com.phlu.bookstore.model.Cliente;
import com.phlu.bookstore.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // --- Cadastro ---
    public void cadastrarUsuario(String nome, String email, String senha, String tipo, String cpf) {
        email = email.trim().toLowerCase();
        Usuario usuario;
        if ("1".equals(tipo)) {
            usuario = new Cliente(nome, email, senha, cpf, false);
            usuarioDAO.adicionarUsuario(usuario, "cliente");
        } else {
            usuario = new Administrador(nome, email, senha, false);
            usuarioDAO.adicionarUsuario(usuario, "administrador");
        }
    }

    public void cadastrarCliente(String nome, String email, String cpf, String senha) {
        email = email.trim().toLowerCase();
        Usuario cliente = new Cliente(nome, email, senha, cpf, false);
        usuarioDAO.adicionarUsuario(cliente, "cliente");
    }

    public void cadastrarAdministradorInicial(String nome, String email, String senha) {
        email = email.trim().toLowerCase();
        if (!usuarioDAO.existeAdministrador()) {
            Administrador admin = new Administrador(nome, email, senha, false);
            usuarioDAO.adicionarUsuario(admin, "administrador");
        }
    }

    // --- Login ---
    public Usuario login(String email, String senha) {
        email = email.trim().toLowerCase();
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null) return null;

        boolean match = usuario.verificarSenha(senha);
        System.out.println("DEBUG: match: " + match);

        if (match) return usuario;
        return null;
    }

    // --- Verificacoes ---
    public boolean existeAdministrador() {
        return usuarioDAO.existeAdministrador();
    }

    // --- Relatorios ---
    public List<String> clientesMaisCompraram() {
        return usuarioDAO.clientesMaisCompraram();
    }
}
