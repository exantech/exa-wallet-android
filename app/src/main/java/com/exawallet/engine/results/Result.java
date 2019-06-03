package com.exawallet.engine.results;

public abstract class Result<T> {
    private final T mResult;

    Result(T result) {
        mResult = result;
    }

    public T getResult() {
        return mResult;
    }

    public boolean isSuccess() {
        return null != mResult;
    }
}