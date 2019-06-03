package com.exawallet.presenters;

import android.text.Editable;
import android.text.TextWatcher;
import com.exawallet.utils.PasswordWatcher;
import com.exawallet.utils.SimpleTextWatcher;
import com.exawallet.views.IRecreateWalletView;

public class RecreateWalletPresenter<V extends IRecreateWalletView> extends ProcessWalletPresenter<V> {
    public TextWatcher getPasswordWatcher() {
        return new PasswordWatcher(mView);
    }

    public TextWatcher getConfirmWatcher() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mView.checkButton();
            }
        };
    }
}