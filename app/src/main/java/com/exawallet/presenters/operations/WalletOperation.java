package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.engine.results.ExceptionResult;
import com.exawallet.presenters.results.WalletResult;

import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.presenters.operations.LoadContentOperation.loadContent;

public abstract class WalletOperation extends SplashScreenOperation<WalletResult> {
    WalletOperation(RootActivity activity) {
        super(activity);
    }

    @Override
    protected final void onFailure(ExceptionResult result) {
        super.onFailure(result);

        walletManager().collapseWallet();
        loadContent(mActivity);
    }
}