package com.devinicius.DsCatolog.resource.exceptions;


import com.devinicius.DsCatolog.services.exceptions.DatabaseExceptions;
import com.devinicius.DsCatolog.services.exceptions.EntityNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNotFoundExceptions.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundExceptions e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        StandardError err = new StandardError();

        err.setTimestamp(Instant.now());
        err.setStatus(httpStatus.value());
        err.setError("Resource not found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(httpStatus).body(err);
    }

    @ExceptionHandler(DatabaseExceptions.class)
    public ResponseEntity<StandardError> databaseException(DatabaseExceptions e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError();

        err.setTimestamp(Instant.now());
        err.setStatus(httpStatus.value());
        err.setError("Database exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(httpStatus).body(err);
    }

}
