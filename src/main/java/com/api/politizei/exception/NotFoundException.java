package com.api.politizei.exception;

public class NotFoundException extends Exception{

    public NotFoundException(String message){
        super(message.concat(" não encontrado"));
    }
}
