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
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(exceptionFilters, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PostLengthException.class)
    public ResponseEntity<ExceptionFilters> postLengthException(PostLengthException exception) {
        ExceptionFilters exceptionFilters = new ExceptionFilters(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(exceptionFilters, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleConflictException.class)
    public ResponseEntity<ExceptionFilters> roleConflictException(RoleConflictException exception) {
        ExceptionFilters exceptionFilters = new ExceptionFilters(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(exceptionFilters, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserInvalid.class)
    public ResponseEntity<?> userInvalid(UserInvalid exception) {
        ExceptionFilters exceptionFilters = new ExceptionFilters(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(exceptionFilters, HttpStatus.BAD_REQUEST);
    }
}
