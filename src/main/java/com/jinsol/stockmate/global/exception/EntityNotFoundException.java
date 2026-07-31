package com.jinsol.stockmate.global.exception;

public class EntityNotFoundException extends  RuntimeException{

    public EntityNotFoundException(String message){
        super(message);
    }
}
