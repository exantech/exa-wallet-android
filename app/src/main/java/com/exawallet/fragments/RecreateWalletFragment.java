package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.RecreateWalletPresenter;
import com.exawallet.views.IRecreateWalletView;

import static java.lang.String.valueOf;

abstract class RecreateWalletFragment<P extends RecreateWalletPresenter> extends ProcessWalletFragment<P, IRecreateWalletView> implements IRecreateWalletView {
    @BindView(R.id.password_layout)
    TextInputLayout mPasswordLayout;

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.confirm)
    EditText mConfirm;

    @BindView(R.id.security)
    View mSecurity;

    private float mWidth;

    boolean isWalletNameValid;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth() - 2 * getResources().getDimension(R.dimen.size63);

        mPassword.addTextChangedListener(mPresenter.getPasswordWatcher());
        mConfirm.addTextChangedListener(mPresenter.getConfirmWatcher());
    }

    public void setSecurity(double security) {
        if (0.01 > security) {
            mSecurity.setBackgroundResource(R.color.shadow);
        } else if (0.4 > security) {
            mSecurity.setBackgroundResource(R.color.red);
        } else if (0.7 > security) {
            mSecurity.setBackgroundResource(R.color.yellow);
        } else {
            mSecurity.setBackgroundResource(R.color.green);
        }

        checkButton();

        ViewGroup.LayoutParams layoutParams = mSecurity.getLayoutParams();
        layoutParams.width = (int) (mWidth * security);
        mSecurity.setLayoutParams(layoutParams);
    }

    @Override
    public void checkWalletName(boolean matches) {
        isWalletNameValid = matches;
    }

    public void checkButton() {
        Editable password = mPassword.getText();
        Editable confirm = mConfirm.getText();
        mContinueButton.setEnabled(isWalletNameValid && valueOf(password).equals(valueOf(confirm)));
    }
}