package com.phlu.bookstore.model;

public class Administrador extends Usuario {

    public Administrador(String nome, String email, String senha, boolean hashed) {
        super(nome, email, senha, hashed);
    }

    @Override
    public void exibirInformacoes() {
        System.out.println("Administrador: " + getNome() + ", Email: " + getEmail());
    }
}
