package com.livraria.usuarios;

/**
 * Classe abstrata que serve de base para Cliente e Administrador.
 * Contém os dados que todo usuário do sistema tem.
 */

public abstract class Usuario {
    protected String nome;
    protected String email;
    protected String cpf;

    public Usuario(String nome, String email, String cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    // Setters (se necessário, para este exemplo, apenas getters)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + ", Email: " + email + ", CPF: " + cpf;
    }
}


