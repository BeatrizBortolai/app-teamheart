package com.teamheart.recrutamento.dto;

import java.time.LocalDateTime;

public record SelecaoResponse(
    Long id,
    String nomeCandidato,
    String tituloVaga,
    String status,
    Character priorizado,
    LocalDateTime dataAvaliacao
) { }

