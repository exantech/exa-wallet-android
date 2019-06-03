package com.exawallet.presenters;

import static com.exawallet.presenters.operations.CreateWalletOperation.createWallet;

public class CreatePersonalWalletPresenter extends CreateWalletPresenter {
    protected void buildWallet(String password, String walletName, String node) {
        createWallet(password, walletName, node, mWalletColor, mLanguage, mActivity);
    }
}