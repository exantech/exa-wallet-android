package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.engine.results.ExceptionResult;

public abstract class NoSplashScreenOperation<R extends ExceptionResult> extends ScreenOperation<R> {
    NoSplashScreenOperation(RootActivity activity) {
        super(activity);
    }

    @Override
    protected final void onStart() {
    }
}
