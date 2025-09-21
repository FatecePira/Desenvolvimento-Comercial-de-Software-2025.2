package com.phlu.bookstore.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class Usuario {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private String nome;
    private String email;
    private String senhaHash;
  
    public Usuario(String nome, String email, String senhaHash, boolean hashed) {
    this.nome = nome;
    this.email = email.trim().toLowerCase();
    if (hashed) {
        this.senhaHash = senhaHash; // usa hash do banco sem gerar novo
    } else {
        this.senhaHash = encoder.encode(senhaHash); // texto puro → hash
    }
}

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senhaHash; }

    public void setSenha(String senha) { this.senhaHash = encoder.encode(senha); }
    public boolean verificarSenha(String senha) { return encoder.matches(senha, this.senhaHash); }

    public abstract void exibirInformacoes();
}
