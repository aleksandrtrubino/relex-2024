package ru.trubino.farm.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.trubino.farm.security.exception.WebSecurityException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class WebSecurityExceptionHandler {

    @ExceptionHandler(value = WebSecurityException.class)
    public ResponseEntity<?> handleResourceNotFoundException(WebSecurityException e){
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put(e.getClass().getSimpleName(),e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
