package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.RestoreWalletPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.RESTORE_WALLET;
import static java.lang.String.valueOf;

public class RestoreWalletFragment extends RecreateWalletFragment<RestoreWalletPresenter> {
    private String mMnemonic;
    private String mLanguage;
    private long mBlockHeight;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListener(mContinueButton, () -> {
            Editable password = mPassword.getText();
            Editable confirm = mConfirm.getText();
            Editable walletName = mWalletName.getText();
            Editable node = mNode.getText();

            if (!isEmpty(walletName) && valueOf(password).equals(valueOf(confirm))) {
                mPresenter.onRestoreWallet(mMnemonic, mBlockHeight, valueOf(password) , valueOf(walletName), valueOf(node), mLanguage);
            }
        });

        BACK_PATH.setScreen(RESTORE_WALLET);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_restore_wallet;
    }

    @Override
    RestoreWalletPresenter createPresenter() {
        return new RestoreWalletPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_restore_wallet, (RootActivity) getActivity());
    }

    private BaseFragment setParameters(String mnemonic, String language, long blockHeight) {
        mMnemonic = mnemonic;
        mBlockHeight = blockHeight;
        mLanguage = language;
        return this;
    }

    public static BaseFragment newInstance() {
        return new RestoreWalletFragment();
    }

    public static BaseFragment newInstance(String mnemonic, String language, long blockHeight) {
        return new RestoreWalletFragment().setParameters(mnemonic, language, blockHeight);
    }
}