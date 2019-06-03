package com.exawallet.presenters.operations;

import com.exawallet.engine.operations.EngineOperation;
import com.exawallet.presenters.results.EmptyResult;

import static com.exawallet.engine.Engine.ENGINE;

public class DelayOperation extends EngineOperation<EmptyResult> {
    private final Runnable mRunnable;

    private DelayOperation(Runnable runnable) {
        this.mRunnable = runnable;
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onSuccess(EmptyResult result) {
    }

    @Override
    protected void onFailure(EmptyResult result) {
    }

    @Override
    protected EmptyResult execute() {
        mRunnable.run();
        return new EmptyResult();
    }

    public static void schedule(Runnable runnable, int timeout) {
        ENGINE.schedule(new DelayOperation(runnable), timeout);
    }
}
