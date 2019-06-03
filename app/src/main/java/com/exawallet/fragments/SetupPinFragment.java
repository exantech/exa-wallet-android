package com.exawallet.fragments;

import com.exawallet.monerowallet.R;
import com.exawallet.presenters.SetupPinPresenter;
import com.exawallet.views.ISetupPinView;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SetupPinFragment extends PinFragment<SetupPinPresenter, ISetupPinView> implements ISetupPinView {
    @Override
    public void showDefaultTitle() {
        mTitle.setText(isPinEmpty() ? R.string.create_pin : R.string.change_pin);
    }

    @Override
    protected SetupPinPresenter createPresenter() {
        return new SetupPinPresenter();
    }

    @Override
    public void confirmPin() {
        setPin0Background(R.drawable.oval_fade_blue);
        setPin1Background(R.drawable.oval_fade_blue);
        setPin2Background(R.drawable.oval_fade_blue);
        setPin3Background(R.drawable.oval_fade_blue);

        mTitle.setText(R.string.confirm_pin);
    }

    private boolean isPinEmpty() {
        boolean isEmpty = true;
        for (int index = 0; isEmpty && index < APP_CONTEXT.getPin().length; index++) {
            isEmpty &= 0 == APP_CONTEXT.getPin()[index];
        }

        return isEmpty;
    }

    public static SetupPinFragment newInstance() {
        return new SetupPinFragment();
    }
}
