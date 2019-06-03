package com.exawallet.common;

public class ErrorMessage implements IEvent {
    private final String mMessage;

    public ErrorMessage(String message) {
        this.mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}