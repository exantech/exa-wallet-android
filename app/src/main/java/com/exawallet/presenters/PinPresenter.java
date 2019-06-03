package com.exawallet.presenters;

import android.os.CountDownTimer;
import android.support.annotation.CallSuper;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IPinView;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public abstract class PinPresenter<V extends IPinView> extends BasePresenter<V> {
    static final int MAX_PIN = 4;
    private int mIndex = 0;
    final byte[] mPin = new byte[MAX_PIN];


    public void onButton0Click() {
        onButtonClick((byte) 0);
    }

    public void onButton1Click() {
        onButtonClick((byte) 1);
    }

    public void onButton2Click() {
        onButtonClick((byte) 2);
    }

    public void onButton3Click() {
        onButtonClick((byte) 3);
    }

    public void onButton4Click() {
        onButtonClick((byte) 4);
    }

    public void onButton5Click() {
        onButtonClick((byte) 5);
    }

    public void onButton6Click() {
        onButtonClick((byte) 6);
    }

    public void onButton7Click() {
        onButtonClick((byte) 7);
    }

    public void onButton8Click() {
        onButtonClick((byte) 8);
    }

    public void onButton9Click() {
        onButtonClick((byte) 9);
    }

    public void onButtonBackClick() {
        if (0 > --mIndex) {
            mIndex = 0;
        }
        viewPin(R.drawable.oval_fade_blue);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        checkPinTime();
    }

    void checkPinTime() {
        if (0 < APP_CONTEXT.getPinTime()) {
            CountDownTimer countDownTimer = new CountDownTimer(APP_CONTEXT.getPinTime(), 1000) {
                public void onTick(long millisUntilFinished) {
                    mView.showWaitTitle(APP_CONTEXT.getPinTime());
                }

                public void onFinish() {
                    mView.showDefaultTitle();
                }
            };

            countDownTimer.start();
        } else {
            mView.showDefaultTitle();
        }
    }

    private synchronized void onButtonClick(byte i) {
        if (0 >= APP_CONTEXT.getPinTime() && mIndex < MAX_PIN) {
            mPin[mIndex] = i;
            viewPin(R.drawable.oval_white);
            if (MAX_PIN == ++mIndex) {
                mIndex = 0;
                onPinEntered();
            }
        }
    }

    private void viewPin(int resourceId) {
        switch (mIndex) {
            case 0: {
                mView.setPin0Background(resourceId);
                break;
            }
            case 1: {
                mView.setPin1Background(resourceId);
                break;
            }
            case 2: {
                mView.setPin2Background(resourceId);
                break;
            }
            case 3: {
                mView.setPin3Background(resourceId);
                break;
            }
        }
    }

    protected abstract void onPinEntered();

    public abstract void onButtonSkipClick();
}
