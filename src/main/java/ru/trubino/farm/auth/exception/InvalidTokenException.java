package ru.trubino.farm.auth.exception;

public class InvalidTokenException extends WebSecurityException {
    public InvalidTokenException(String message){
        super(message);
    }
}
