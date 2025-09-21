package com.phlu.bookstore.service;

import com.phlu.bookstore.dao.LivroDAO;
import com.phlu.bookstore.dao.UsuarioDAO;

import java.util.List;

public class RelatorioService {

    private LivroDAO livroDAO;
    private UsuarioDAO usuarioDAO;

    public RelatorioService() {
        this.livroDAO = new LivroDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    public List<String> gerarLivrosMaisVendidos() {
        return livroDAO.livrosMaisVendidos();
    }

    public List<String> gerarClientesMaisCompraram() {
        return usuarioDAO.clientesMaisCompraram();
    }
}
