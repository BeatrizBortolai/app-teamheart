package com.teamheart.recrutamento.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_VAGA")
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;
    private String departamento;
    private String nivel;

    @Column(name = "META_DIVERSIDADE")
    private Integer metaDiversidade;

    @Column(name = "DATA_ABERTURA")
    private LocalDateTime dataAbertura = LocalDateTime.now();

    public Vaga(Long id, LocalDateTime dataAbertura, Integer metaDiversidade, String nivel,
                String departamento, String descricao, String titulo) {
        this.id = id;
        this.dataAbertura = dataAbertura;
        this.metaDiversidade = metaDiversidade;
        this.nivel = nivel;
        this.departamento = departamento;
        this.descricao = descricao;
        this.titulo = titulo;
    }

    public Vaga() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Integer getMetaDiversidade() {
        return metaDiversidade;
    }

    public void setMetaDiversidade(Integer metaDiversidade) {
        this.metaDiversidade = metaDiversidade;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

}
