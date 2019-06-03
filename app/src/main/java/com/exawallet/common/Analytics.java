package com.exawallet.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;

public enum Analytics {
    ANALYTICS;

    private FirebaseAnalytics mFirebaseAnalytics;
    private Activity mActivity;

    public void init(Context context, Activity activity) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mActivity = activity;
    }

    public void logScreen(String screen, String action) {
        mFirebaseAnalytics.setCurrentScreen(mActivity, screen, action);
    }

    public void logException(String screen, String action, Exception exception) {
        Bundle bundle = new Bundle();

        bundle.putString("action", action);
        bundle.putSerializable("exception", exception);

        mFirebaseAnalytics.logEvent(screen, bundle);
    }

    public void logEvent(String screen, String action) {
        Bundle bundle = new Bundle();

        bundle.putString("action", action);

        mFirebaseAnalytics.logEvent(screen, bundle);
    }
}
