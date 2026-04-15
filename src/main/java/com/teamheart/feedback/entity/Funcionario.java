package com.teamheart.feedback.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "T_FUNCIONARIO")
public class Funcionario {

    @Id
    @Column(name = "ID_FUNCIONARIO", length = 36)
    private UUID id;

    @NotBlank(message = "O nome do funcionário é obrigatório")
    @Size(max = 100, message = "O nome do funcionário deve ter no máximo 100 caracteres")
    @Column(name = "NM_FUNCIONARIO", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O gênero é obrigatório")
    @Column(name = "DS_GENERO", nullable = false, length = 20)
    private String genero;

    @NotNull(message = "O campo PCD é obrigatório")
    @Column(name = "FL_PCD", nullable = false)
    private Boolean pcd;

    @NotBlank(message = "O cargo é obrigatório")
    @Column(name = "DS_CARGO", nullable = false, length = 100)
    private String cargo;

    @Column(name = "DS_ETNIA", length = 50)
    private String etnia;

    @Column(name = "NU_IDADE")
    private Integer idade;

    @Column(name = "DS_DEPARTAMENTO", length = 100)
    private String departamento;

    @Column(name = "DT_CONTRATACAO")
    private LocalDate dataContratacao;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    public Funcionario() {
        this.id = UUID.randomUUID();
        this.dataContratacao = LocalDate.now();
    }

    public Funcionario(String nome, String genero, Boolean pcd, String cargo) {
        this();
        this.nome = nome;
        this.genero = genero;
        this.pcd = pcd;
        this.cargo = cargo;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Boolean getPcd() { return pcd; }
    public void setPcd(Boolean pcd) { this.pcd = pcd; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getEtnia() { return etnia; }
    public void setEtnia(String etnia) { this.etnia = etnia; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }

    public List<Feedback> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks = feedbacks; }
}
