package ru.trubino.farm.security.exception;

public class WebSecurityException extends RuntimeException{
    public WebSecurityException(String message){
        super(message);
    }
}
