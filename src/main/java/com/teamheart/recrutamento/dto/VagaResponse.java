package com.teamheart.recrutamento.dto;


import java.time.LocalDateTime;

public record VagaResponse(
    Long id,
    String titulo,
    String descricao,
    String departamento,
    String nivel,
    Integer metaDiversidade,
    LocalDateTime dataAbertura
) {}