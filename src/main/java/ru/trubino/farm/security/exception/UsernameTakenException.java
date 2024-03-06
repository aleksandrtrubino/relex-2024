package ru.trubino.farm.security.exception;

public class UsernameTakenException extends WebSecurityException {
    public UsernameTakenException(String message){
        super(message);
    }
}
