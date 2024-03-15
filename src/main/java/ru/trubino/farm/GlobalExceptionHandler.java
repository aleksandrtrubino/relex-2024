package ru.trubino.farm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.trubino.farm.auth.exception.CookieNotFoundException;
import ru.trubino.farm.authority.exception.AuthorityNotFoundException;
import ru.trubino.farm.product.exception.ProductAlreadyExistsException;
import ru.trubino.farm.product.exception.ProductNotFoundException;
import ru.trubino.farm.unit.exception.UnitAlreadyExistsException;
import ru.trubino.farm.unit.exception.UnitNotFoundException;
import ru.trubino.farm.user.exception.UserAlreadyExistsException;
import ru.trubino.farm.user.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, UnitNotFoundException.class, ProductNotFoundException.class, AuthorityNotFoundException.class, CookieNotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(RuntimeException e){
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put("exception",e.getClass().getSimpleName());
        responseBody.put("message",e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UnitAlreadyExistsException.class, ProductAlreadyExistsException.class})
    public ResponseEntity<?> handleAlreadyExistsException(RuntimeException e){
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put("exception",e.getClass().getSimpleName());
        responseBody.put("message",e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
