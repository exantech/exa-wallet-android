package com.exawallet.presenters;

import com.exawallet.fragments.CreatePersonalWalletFragment;
import com.exawallet.fragments.CreateSharedWalletFragment;
import com.exawallet.fragments.InsertInviteCodeFragment;
import com.exawallet.fragments.RestoreMnemonicFragment;
import com.exawallet.views.IBaseView;

public class AccessWalletPresenter extends BasePresenter<IBaseView> {
    public void onCreatePersonalWallet() {
        mActivity.show(CreatePersonalWalletFragment.newInstance());
    }

    public void onCreateMultisigWallet() {
        mActivity.show(CreateSharedWalletFragment.newInstance());
    }

    public void onJoinWallet() {
        mActivity.show(InsertInviteCodeFragment.newInstance());
    }

    public void onRestoreWallet() {
        mActivity.show(RestoreMnemonicFragment.newInstance());
    }
}