package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import butterknife.BindView;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.CreateWalletPresenter;

import static android.text.TextUtils.isEmpty;
import static java.lang.String.valueOf;

public abstract class CreateWalletFragment<P extends CreateWalletPresenter> extends RecreateWalletFragment<P> {
    @BindView(R.id.language)
    Spinner mLanguageSpinner;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLanguageSpinner.setAdapter(new ArrayAdapter(getContext(), R.layout.spinner_item, getResources().getStringArray(R.array.languages)));
        mLanguageSpinner.setSelection(0);
        mLanguageSpinner.setOnItemSelectedListener(mPresenter.OnSelectLanguage());

        setOnClickListener(mContinueButton, () -> {
            Editable password = mPassword.getText();
            Editable confirm = mConfirm.getText();
            Editable walletName = mWalletName.getText();
            Editable node = mNode.getText();

            if (!isEmpty(walletName) && !isEmpty(node) && valueOf(password).equals(valueOf(confirm))) {
                mPresenter.onCreateWallet(valueOf(password), valueOf(walletName), valueOf(node));
            }
        });
    }
}