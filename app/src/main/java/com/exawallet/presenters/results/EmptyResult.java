package com.exawallet.presenters.results;

import com.exawallet.engine.results.ExceptionResult;

public class EmptyResult<T> extends ExceptionResult<T> {
    public EmptyResult(Exception exception) {
        super(exception);
    }

    public EmptyResult() {
        super((T) null);
    }

    @Override
    public boolean isSuccess() {
        return null == getResult();
    }
}