package com.exawallet.presenters;

import static com.exawallet.presenters.operations.RestoreWalletOperation.restoreWallet;

public class RestoreWalletPresenter extends RecreateWalletPresenter {
    public void onRestoreWallet(String mnemonic, long blockHeight, String password, String walletName, String node, String language) {
        restoreWallet(mnemonic, blockHeight, password, walletName, node, mWalletColor, mActivity, language);
    }
}