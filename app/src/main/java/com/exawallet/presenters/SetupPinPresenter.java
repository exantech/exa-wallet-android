package com.exawallet.presenters;

import android.content.Context;
import android.os.Vibrator;
import com.exawallet.views.ISetupPinView;

import java.util.Arrays;

import static com.exawallet.common.AppContext.APP_CONTEXT;
import static java.lang.System.arraycopy;

public class SetupPinPresenter extends PinPresenter<ISetupPinView> {
    private boolean mConfirmMode = false;

    private final byte[] mPin0 = new byte[MAX_PIN];

    @Override
    protected void onPinEntered() {
        if (mConfirmMode) {
            if (Arrays.equals(mPin0, mPin)) {
                APP_CONTEXT.setPin(mPin);
                done();
            } else {
                ((Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(400);
                mView.confirmPin();
            }
        } else {
            arraycopy(mPin, 0, mPin0, 0, MAX_PIN);
            mConfirmMode = true;
            mView.confirmPin();
        }
    }

    @Override
    public void onButtonSkipClick() {
        done();
    }

    private void done() {
        APP_CONTEXT.notifyChange();
        mActivity.goCurrent();
    }
}