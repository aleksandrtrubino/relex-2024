package ru.trubino.farm.auth.exception;

public class WebSecurityException extends RuntimeException{
    public WebSecurityException(String message){
        super(message);
    }
}
