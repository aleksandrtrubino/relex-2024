package ru.trubino.farm.auth.exception;

public class AbsentTokenException extends WebSecurityException {
    public AbsentTokenException(String message){
        super(message);
    }
}
