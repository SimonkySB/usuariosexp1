package com.simonky.usuarios.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class DireccionNotFoundException extends ErrorResponseException{
    public DireccionNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("La dirección con ID: " + id + " no fue encontrada" ), null);
        
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Dirección no encontrada");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
