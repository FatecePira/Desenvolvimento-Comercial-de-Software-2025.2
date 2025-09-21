package org.example.model;

public class Administrador extends Usuario {

    public Administrador(String nome, String email, String cpf) {
        super(nome, email, cpf);
        this.setTipoUsuario(TipoUsuario.ADMIN);
    }

    @Override
    public String toString() {
        return String.format("Administrador: %s (%s)", getNome(), getEmail());
    }
}
