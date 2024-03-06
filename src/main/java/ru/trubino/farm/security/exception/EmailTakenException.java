package ru.trubino.farm.security.exception;

public class EmailTakenException extends WebSecurityException {
    public EmailTakenException(String message){
        super(message);
    }
}
