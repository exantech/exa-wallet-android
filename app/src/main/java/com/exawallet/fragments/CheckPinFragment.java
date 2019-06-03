package com.exawallet.fragments;

import com.exawallet.monerowallet.R;
import com.exawallet.presenters.CheckPinPresenter;
import com.exawallet.presenters.PinPresenter;

public class CheckPinFragment extends PinFragment {
    @Override
    public void showDefaultTitle() {
        mTitle.setText(R.string.enter_pin);
    }

    @Override
    protected PinPresenter createPresenter() {
        return new CheckPinPresenter();
    }

    public static CheckPinFragment newInstance() {
        return new CheckPinFragment();
    }
}