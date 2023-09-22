package com.api.politizei.exception;

public class BusinessSecurityException extends Exception{

    public BusinessSecurityException(String message) {
        super(message);
    }

    public BusinessSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
