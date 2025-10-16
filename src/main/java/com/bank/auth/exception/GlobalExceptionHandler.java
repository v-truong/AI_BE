package com.bank.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntime(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

    public static class ErrorDto {
        private String error;
        public ErrorDto(){}
        public ErrorDto(String error){ this.error = error; }
        public String getError(){ return error; }
        public void setError(String error){ this.error = error; }
    }
}
