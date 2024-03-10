package ru.trubino.farm.auth.exception;

public class CookieNotFoundException extends RuntimeException {
    public CookieNotFoundException(String message){
        super(message);
    }
}
