package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.engine.operations.EngineOperation;
import com.exawallet.engine.results.ExceptionResult;
import com.exawallet.utils.HLog;

import static com.exawallet.common.Analytics.ANALYTICS;

public abstract class ScreenOperation<R extends ExceptionResult> extends EngineOperation<R> {
    final RootActivity mActivity;

    ScreenOperation(RootActivity activity) {
        this.mActivity = activity;

        ANALYTICS.logEvent(TAG, "onCreate");
    }

    @Override
    protected void onFailure(ExceptionResult result) {
        ANALYTICS.logException(TAG, "onFailure", result.getException());

        HLog.debug(TAG, "onFailure " + result.getExceptionMessage());
        mActivity.goCurrent(result.getExceptionMessage());
    }
}