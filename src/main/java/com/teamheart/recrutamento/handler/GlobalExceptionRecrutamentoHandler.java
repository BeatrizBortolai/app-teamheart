package com.teamheart.recrutamento.handler;

import com.teamheart.common.api.ApiErrorResponse;
import com.teamheart.recrutamento.exception.CandidatoNotFoundException;
import com.teamheart.recrutamento.exception.RecursoNaoEncontradoException;
import com.teamheart.recrutamento.exception.VagaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionRecrutamentoHandler {

    @ExceptionHandler({RecursoNaoEncontradoException.class, CandidatoNotFoundException.class, VagaNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado", ex.getMessage()));
    }
}
