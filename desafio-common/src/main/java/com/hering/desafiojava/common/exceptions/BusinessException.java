package com.hering.desafiojava.common.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final HttpStatus status;

    public BusinessException(String message) {
        this(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status != null ? status : HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public HttpStatus getStatus() {
        return status;
    }
}