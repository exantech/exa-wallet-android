package com.exawallet.engine.results;

public abstract class ExceptionResult<T> extends Result<T> {
    private Exception mException = null;

    protected ExceptionResult(Exception exception) {
        super(null);
        mException = exception;
    }

    protected ExceptionResult(T result) {
        super(result);
    }

    public Exception getException() {
        return mException;
    }

    public String getExceptionMessage() {
        return null != mException && null != mException.getLocalizedMessage() ? mException.getLocalizedMessage() : "";
    }
}