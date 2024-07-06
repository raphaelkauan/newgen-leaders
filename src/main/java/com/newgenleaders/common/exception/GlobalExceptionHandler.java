package com.newgenleaders.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserConflictException.class)
    public ResponseEntity<ExceptionFilters> userConflictException(UserConflictException exception) {
        ExceptionFilters exceptionFilters = new ExceptionFilters(
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(exceptionFilters, HttpStatus.CONFLICT);
    }

}
