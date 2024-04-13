package com.simonky.usuarios.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class EmailExistsException extends ErrorResponseException{
    
    public EmailExistsException(String email) {
        super(HttpStatus.BAD_REQUEST, asProblemDetail("El email " + email + " ya se encuentra en uso" ), null);
        
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        problemDetail.setTitle("El email existe");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
