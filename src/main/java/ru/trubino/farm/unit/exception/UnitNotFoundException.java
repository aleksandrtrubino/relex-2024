package ru.trubino.farm.unit.exception;

public class UnitNotFoundException extends RuntimeException{
    public UnitNotFoundException(String message){
        super(message);
    }
}