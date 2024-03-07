package ru.trubino.farm.auth.exception;

public class EmailTakenException extends WebSecurityException {
    public EmailTakenException(String message){
        super(message);
    }
}
