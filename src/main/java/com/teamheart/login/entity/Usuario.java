package com.teamheart.login.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "T_USUARIO")
public class Usuario {

    @Id
    @Column(name = "ID_USER", length = 36)
    private String id;

    @Column(name = "DS_EMAIL", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "DS_SENHA", nullable = false, length = 255)
    private String senha;

    @Column(name = "NM_USER", nullable = false, length = 255)
    private String nome;

    public Usuario() {
        this.id = UUID.randomUUID().toString();
    }

    public Usuario(String nome, String email, String senha) {
        this();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
