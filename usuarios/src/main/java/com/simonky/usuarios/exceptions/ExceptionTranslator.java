package com.simonky.usuarios.exceptions;


import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;

@RestControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

    @Builder private record InvalidatedParams (String cause, String attribute) {}

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleConstraintViolationException(ConstraintViolationException e) {

        Set<ConstraintViolation<?>> errors = e.getConstraintViolations();
        List<InvalidatedParams> validationResponse = errors.stream()
            .map(err -> InvalidatedParams.builder()
                .cause(err.getMessage())
                .attribute(err.getPropertyPath().toString())
                .build()
            ).toList();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Error en validacion de datos");
        problemDetail.setTitle("Error en validacion de datos");
        problemDetail.setProperty("errors", validationResponse);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        List<InvalidatedParams> validationResponse = errors.stream()
            .map(err -> InvalidatedParams.builder()
                .cause(err.getDefaultMessage())
                .attribute(err.getField())
                .build()
            ).toList();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Error en validacion de datos");
        problemDetail.setTitle("Error en validacion de datos");
        problemDetail.setProperty("errors", validationResponse);
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
     }
}