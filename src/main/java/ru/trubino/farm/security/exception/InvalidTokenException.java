package ru.trubino.farm.security.exception;

public class InvalidTokenException extends WebSecurityException {
    public InvalidTokenException(String message){
        super(message);
    }
}
