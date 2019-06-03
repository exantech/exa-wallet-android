package com.exawallet.presenters;

import android.content.Context;
import android.os.Vibrator;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IPinView;

import java.util.Arrays;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class CheckPinPresenter extends PinPresenter<IPinView> {

    @Override
    protected void onPinEntered() {
        if (Arrays.equals(APP_CONTEXT.getPin(), mPin)) {
            APP_CONTEXT.notifyPinAttempt(true);
            mActivity.goCurrent();
        } else {
            APP_CONTEXT.notifyPinAttempt(false);

            mView.setPin0Background(R.drawable.oval_fade_blue);
            mView.setPin1Background(R.drawable.oval_fade_blue);
            mView.setPin2Background(R.drawable.oval_fade_blue);
            mView.setPin3Background(R.drawable.oval_fade_blue);

            checkPinTime();

            ((Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(400);
        }
    }

    @Override
    public void onButtonSkipClick() {

    }
}
