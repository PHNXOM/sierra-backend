package com.sierra.backend.exception;

import com.sierra.backend.dto.ServerMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ServerMessageResponse> handleCustomException(ServerException exception) {
        HttpStatus status = exception.getStatus();
        String message = exception.getMessage();

        return new ResponseEntity<>(new ServerMessageResponse(message), status);
    }

    @ExceptionHandler
    public ResponseEntity<ServerMessageResponse> handleValidationException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getFieldError();

        String message = (fieldError != null) ? fieldError.getDefaultMessage() : "Validation Error";

        return new ResponseEntity<>(new ServerMessageResponse(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ServerMessageResponse> handleAllExceptions(Exception exception) {
        return new ResponseEntity<>(new ServerMessageResponse("An internal server error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
