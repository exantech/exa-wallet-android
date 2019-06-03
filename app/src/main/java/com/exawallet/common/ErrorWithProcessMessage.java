package com.exawallet.common;

public class ErrorWithProcessMessage implements IEvent {
    private final String mMessage;
    private final int mTitle;
    private final Runnable mRunnable;

    public ErrorWithProcessMessage(String message, int title, Runnable runnable) {
        this.mMessage = message;
        this.mTitle = title;
        this.mRunnable = runnable;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getTitle() {
        return mTitle;
    }

    public Runnable getRunnable() {
        return mRunnable;
    }
}