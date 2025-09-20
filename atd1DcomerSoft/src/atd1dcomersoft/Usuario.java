/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package atd1dcomersoft;

/**
 *
 * @author yasmi
 */
// Classe abstrata Usuario - implementa herança e polimorfismo
abstract class Usuario {
protected String nome;
protected String email;
protected String cpf;

/**
 * Construtor da classe Usuario
 * @param nome Nome do usuário
 * @param email E-mail do usuário
 * @param cpf CPF do usuário
 */
public Usuario(String nome, String email, String cpf) {
    this.nome = nome;
    this.email = email;
    this.cpf = cpf;
}

// Getters e Setters - Encapsulamento
public String getNome() { return nome; }
public void setNome(String nome) { this.nome = nome; }

public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }

public String getCpf() { return cpf; }
public void setCpf(String cpf) { this.cpf = cpf; }

// Método abstrato - será implementado nas classes filhas (polimorfismo)
public abstract void exibirInformacoes();

}