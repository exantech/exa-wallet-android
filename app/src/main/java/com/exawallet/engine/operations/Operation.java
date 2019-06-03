package com.exawallet.engine.operations;

import com.exawallet.engine.results.Result;

public abstract class Operation<R extends Result> {
    protected static String TAG = Operation.class.getSimpleName();

    protected Operation() {
        TAG = this.getClass().getSimpleName();
    }

    protected abstract void onStart();

    protected abstract void onSuccess(R result);

    protected abstract void onFailure(R result);
}