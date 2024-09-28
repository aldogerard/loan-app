package com.enigmacamp.loanapp.controller;

import com.enigmacamp.loanapp.base.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> responseStatusException(ResponseStatusException e) {
        String reason = Objects.requireNonNull(e.getReason()).split(" \"")[1];
        BaseResponse<?> response = BaseResponse.builder()
                .statusCode(e.getStatusCode().value())
                .message(reason.substring(0, reason.length() - 1))
                .build();

        return ResponseEntity
                .status(e.getStatusCode())
                .body(response);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> responseStatusException(ConstraintViolationException e) {
        BaseResponse<?> response = BaseResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
