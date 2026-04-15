package com.teamheart.feedback.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "T_FEEDBACK")
public class Feedback {

    @Id
    @Column(name = "ID_FEEDBACK", length = 36)
    private UUID id;

    @Column(name = "DS_SENTIMENTO", nullable = false, length = 20)
    private String sentimento;

    @Column(name = "DS_COMENTARIO", length = 500)
    private String comentario;

    @Column(name = "DT_FEEDBACK", nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "ID_FUNCIONARIO", nullable = false)
    private Funcionario funcionario;

    public Feedback() {
        this.id = UUID.randomUUID();
        this.data = LocalDate.now();
    }

    public Feedback(String sentimento, String comentario, Funcionario funcionario) {
        this();
        this.sentimento = sentimento;
        this.comentario = comentario;
        this.funcionario = funcionario;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getSentimento() { return sentimento; }
    public void setSentimento(String sentimento) { this.sentimento = sentimento; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
}
