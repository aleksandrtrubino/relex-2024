package ru.trubino.farm.auth.exception;

public class UsernameTakenException extends WebSecurityException {
    public UsernameTakenException(String message){
        super(message);
    }
}
