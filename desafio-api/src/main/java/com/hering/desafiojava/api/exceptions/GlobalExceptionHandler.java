package com.hering.desafiojava.api.exceptions;

import com.hering.desafiojava.common.exceptions.AppError;
import com.hering.desafiojava.common.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<AppError> handleBusiness(BusinessException ex, HttpServletRequest req) {
        HttpStatus status = ex.getStatus();
        return ResponseEntity
                .status(status)
                .body(new AppError(
                        "Business Error",
                        ex.getMessage(),
                        status.value(),
                        req.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> handleGeneric(Exception ex, HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AppError(
                        "Internal Error",
                        "Erro inesperado. Tente novamente.",
                        500,
                        req.getRequestURI(),
                        LocalDateTime.now()
                ));
    }
}