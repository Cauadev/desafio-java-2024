package com.hering.desafiojava.common.exceptions;

import java.time.LocalDateTime;

public class AppError {

    private String error;
    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    public AppError(String error, String message, int status, String path, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
