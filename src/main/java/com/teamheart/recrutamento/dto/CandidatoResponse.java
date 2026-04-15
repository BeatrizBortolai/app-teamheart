package com.teamheart.recrutamento.dto;

import java.time.LocalDateTime;

public record CandidatoResponse(
    Long id,
    String nome,
    String email,
    String genero,
    String etnia,
    String localizacao,
    Integer experienciaAnos,
    LocalDateTime dataRegistro
) {}
