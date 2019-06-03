package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.model.Wallet;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.presenters.results.WalletResult;
import com.exawallet.utils.HLog;

import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.WalletManager.walletManager;

public class StoreWalletOperation extends WalletOperation {
    private final Runnable mRunnable;

    private StoreWalletOperation(Runnable runnable, RootActivity activity) {
        super(activity);
        this.mRunnable = runnable;
    }

    @Override
    protected void onSuccess(WalletResult result) {
        HLog.debug(TAG, "onSuccess " + (null != mRunnable));
        if (null != mRunnable) {
            mRunnable.run();
        } else {
            mActivity.goBack();
        }
    }

    @Override
    protected WalletResult execute() {
        try {
            HLog.debug(TAG, "execute");

            Wallet wallet = walletManager().getWallet();
            WalletMeta walletMeta = wallet.getWalletMeta();
            walletMeta.store();
            wallet.store();

            return new WalletResult(wallet);
        } catch (Exception e) {
            return new WalletResult(e);
        }
    }

    public static void storeWallet(Runnable runnable, RootActivity activity) {
        ENGINE.submit(new StoreWalletOperation(runnable, activity));
    }
}