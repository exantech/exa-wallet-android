package com.exawallet.presenters;

import com.exawallet.RootActivity;
import com.exawallet.model.Wallet;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.views.IBaseView;

import static com.exawallet.model.WalletManager.walletManager;

public abstract class AttachedPresenter<V extends IBaseView> extends BasePresenter<V> {
    Wallet mWallet;
    WalletMeta mWalletMeta;

    @Override
    public void onAttach(RootActivity activity) {
        super.onAttach(activity);

        mWallet = walletManager().getWallet();
        mWalletMeta = mWallet.getWalletMeta();
    }
}