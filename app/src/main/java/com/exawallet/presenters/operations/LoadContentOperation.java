package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.fragments.AccessWalletFragment;
import com.exawallet.fragments.WalletsFragment;
import com.exawallet.presenters.results.ContentResult;
import com.exawallet.utils.HLog;

import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.WalletManager.walletManager;
import static com.google.common.collect.Iterables.isEmpty;

public class LoadContentOperation extends NoSplashScreenOperation<ContentResult> {
    private LoadContentOperation(RootActivity activity) {
        super(activity);
    }

    @Override
    protected void onSuccess(ContentResult result) {
        HLog.debug(TAG, "onSuccess");

        if (isEmpty(result.getResult())) {
            mActivity.show(AccessWalletFragment.newInstance());
        } else {
            mActivity.show(WalletsFragment.newInstance(result.getResult()));
        }
    }

    @Override
    protected ContentResult execute() {
        return new ContentResult(walletManager().findWallets(mActivity.getFilesDir()));
    }

    public static void loadContent(RootActivity activity) {
        ENGINE.submit(new LoadContentOperation(activity));
    }
}