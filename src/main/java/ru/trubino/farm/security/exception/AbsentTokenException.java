package ru.trubino.farm.security.exception;

public class AbsentTokenException extends WebSecurityException {
    public AbsentTokenException(String message){
        super(message);
    }
}
