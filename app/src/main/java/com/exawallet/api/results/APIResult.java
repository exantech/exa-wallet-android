package com.exawallet.api.results;

import com.exawallet.engine.results.ExceptionResult;

public class APIResult<T> extends ExceptionResult<T> {
    public APIResult(Exception exception) {
        super(exception);
    }

    public APIResult(T result) {
        super(result);
    }

    @Override
    public boolean isSuccess() {
        return null == getException();
    }
}
