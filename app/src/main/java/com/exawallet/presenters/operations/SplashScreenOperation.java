package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.engine.results.ExceptionResult;
import com.exawallet.fragments.SplashFragment;
import com.exawallet.utils.HLog;

import static com.exawallet.common.Analytics.ANALYTICS;

public abstract class SplashScreenOperation<R extends ExceptionResult> extends ScreenOperation<R> {
    SplashScreenOperation(RootActivity activity) {
        super(activity);
    }

    @Override
    protected final void onStart() {
        mActivity.show(SplashFragment.newInstance());
    }

    @Override
    protected void onFailure(ExceptionResult result) {
        ANALYTICS.logException(TAG, "onFailure" , result.getException());

        HLog.debug(TAG, "onFailure " + result.getExceptionMessage());
        mActivity.goBack(result.getExceptionMessage());
    }
}