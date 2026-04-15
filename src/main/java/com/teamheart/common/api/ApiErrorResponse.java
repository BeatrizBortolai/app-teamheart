package com.teamheart.common.api;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    Map<String, String> fields
) {
    public ApiErrorResponse(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, null);
    }

    public ApiErrorResponse(int status, String error, String message, Map<String, String> fields) {
        this(LocalDateTime.now(), status, error, message, fields);
    }
}
