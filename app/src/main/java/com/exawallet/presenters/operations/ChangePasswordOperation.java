package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.model.Wallet;
import com.exawallet.presenters.results.WalletResult;
import com.exawallet.utils.HLog;

import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.WalletManager.walletManager;

public class ChangePasswordOperation extends WalletOperation {
    private final String mPassword;

    private ChangePasswordOperation(RootActivity activity, String password) {
        super(activity);
        this.mPassword = password;
    }

    @Override
    protected void onSuccess(WalletResult result) {
        HLog.debug(TAG, "onSuccess");
        mActivity.goBack();
    }

    @Override
    protected WalletResult execute() {
        try {
            Wallet wallet = walletManager().getWallet();
            wallet.setPassword(mPassword);
            wallet.store();

            return new WalletResult(wallet);
        } catch (Exception e) {
            return new WalletResult(e);
        }
    }

    public static void changePassword(RootActivity activity, String password){
        ENGINE.submit(new ChangePasswordOperation(activity, password));
    }
}