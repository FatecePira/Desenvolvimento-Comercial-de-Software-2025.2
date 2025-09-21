package com.livraria.usuarios;

public class Administrador extends Usuario {

    public Administrador(String nome, String email, String cpf) {
        super(nome, email, cpf);
    }

    // Métodos específicos do administrador, podem ser adicionados no futuro aqui.
    // Por enquanto, herda as propriedades e comportamentos básicos de Usuario.

    @Override
    public String toString() {
        return "Administrador - " + super.toString();
    }
}


