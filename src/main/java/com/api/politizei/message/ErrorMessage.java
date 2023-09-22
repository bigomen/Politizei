package com.api.politizei.message;

public class ErrorMessage implements IBaseMessage{

    private final String message;
    private final String status;

    public ErrorMessage(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public boolean getError() {
        return true;
    }
}
