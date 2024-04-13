package com.simonky.usuarios.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class UsuarioNotFoundException extends ErrorResponseException {


    public UsuarioNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("El usuario con ID: " + id + " no fue encontrado" ), null);
        
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Usuario no encontrado");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
