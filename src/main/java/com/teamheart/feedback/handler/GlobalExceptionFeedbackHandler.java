package com.teamheart.feedback.handler;

import com.teamheart.common.api.ApiErrorResponse;
import com.teamheart.feedback.exception.FeedbackNaoEncontradoException;
import com.teamheart.feedback.exception.FuncionarioNaoEncontradoException;
import com.teamheart.login.exception.EmailJaCadastradoException;
import com.teamheart.login.exception.UsuarioNaoEncontradoException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionFeedbackHandler {

    @ExceptionHandler(FeedbackNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleFeedbackNotFound(FeedbackNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado", ex.getMessage()));
    }

    @ExceptionHandler({FuncionarioNaoEncontradoException.class, UsuarioNaoEncontradoException.class})
    public ResponseEntity<ApiErrorResponse> handleRelacionamentoOuUsuarioNaoEncontrado(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado", ex.getMessage()));
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(HttpStatus.CONFLICT.value(), "Conflito", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fields.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Erro de validação",
            "Um ou mais campos estão inválidos.",
            fields
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(
            new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), "Erro de validação", ex.getMessage())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage() != null && ex.getMessage().contains("Invalid UUID")
            ? "ID fornecido é inválido (formato UUID incorreto)."
            : ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), "Requisição inválida", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno", "Ocorreu um erro interno no servidor."));
    }
}
