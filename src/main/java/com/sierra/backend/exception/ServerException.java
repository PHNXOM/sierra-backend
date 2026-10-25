package com.sierra.backend.exception;

import org.springframework.http.HttpStatus;

public class ServerException extends RuntimeException {
    private HttpStatus status;

    public ServerException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
