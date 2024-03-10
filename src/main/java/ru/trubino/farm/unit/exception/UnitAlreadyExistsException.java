package ru.trubino.farm.unit.exception;

public class UnitAlreadyExistsException extends RuntimeException{
    public UnitAlreadyExistsException(String message){
        super(message);
    }
}
