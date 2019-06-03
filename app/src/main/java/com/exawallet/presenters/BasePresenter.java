package com.exawallet.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.exawallet.RootActivity;
import com.exawallet.common.ErrorMessage;
import com.exawallet.common.ErrorWithProcessMessage;
import com.exawallet.monerowallet.R;
import com.exawallet.utils.HLog;
import com.exawallet.views.IBaseView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.utils.BackPath.BACK_PATH;

public abstract class BasePresenter<V extends IBaseView> {
    static String TAG = "BasePresenter";

    private final EventBus mEventBus = EventBus.getDefault();

    V mView;
    RootActivity mActivity;

    BasePresenter() {
        TAG = this.getClass().getSimpleName();
    }

    public void onCreate(V view) {
        mView = view;
    }

    private void subscribe() {
        try {
            if (!mEventBus.isRegistered(this)) {
                mEventBus.register(this);
            }
        } catch (EventBusException e) {
            HLog.verbose(TAG, TAG + " is not registered to EventBus");
        }
    }

    private void unsubscribe() {
        try {
            if (mEventBus.isRegistered(this)) {
                mEventBus.unregister(this);
            }
        } catch (EventBusException e) {
            HLog.verbose(TAG, TAG + " is not unregistered from EventBus");
        }
    }

    @Subscribe
    public void onEvent(final ErrorMessage event) {
        mView.showErrorDialog(getString(R.string.error), event.getMessage());
    }

    @Subscribe
    public void onEvent(final ErrorWithProcessMessage event) {
        mView.showErrorDialog(getString(R.string.error), event.getTitle(), event.getMessage(), event.getRunnable());
    }

    public void onAttach(RootActivity activity) {
        mActivity = activity;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        subscribe();
    }

    public void onStart() {
    }

    public void onResume() {
        String message = BACK_PATH.getMessage();
        if (!isEmpty(message)) {
            mView.showErrorDialog(getString(R.string.error), message);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onPause() {
        ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Fragment) mView).getView().getWindowToken(), 0);
    }

    public void onStop() {
    }

    public void onDestroyView() {
        unsubscribe();
    }

    public void onDestroy() {
    }

    public void onDetach() {
    }

    String getString(int string) {
        return mActivity.getResources().getString(string);
    }
}