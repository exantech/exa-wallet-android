package com.exawallet.presenters;

import android.text.Editable;
import android.text.TextWatcher;
import com.exawallet.utils.PasswordWatcher;
import com.exawallet.utils.SimpleTextWatcher;
import com.exawallet.views.IChangePasswordView;

import static com.exawallet.presenters.operations.ChangePasswordOperation.changePassword;

public class ChangePasswordPresenter extends BasePresenter<IChangePasswordView> {
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

    public void onChangePassword(String text) {
        changePassword(mActivity, text);
    }
}