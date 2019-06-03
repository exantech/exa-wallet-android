package com.exawallet.presenters;

import com.exawallet.common.WalletNewBlock;
import com.exawallet.common.WalletRefreshed;
import com.exawallet.utils.HLog;
import com.exawallet.views.ISyncSplashView;
import org.greenrobot.eventbus.Subscribe;

import static com.exawallet.model.WalletManager.walletManager;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class SyncSplashPresenter extends BasePresenter<ISyncSplashView> {
    private boolean isDone = false;

    public SyncSplashPresenter() {
        super();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        HLog.debug(TAG, "onDetach " + isDone);

        if (!isDone) {
            walletManager().stopRefresh();
        }
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(final WalletRefreshed event) {
        isDone = true;

        mActivity.goBack();
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(final WalletNewBlock event) {
        mView.setProgress(event.getRemaining());
        mView.notifyConnection(event.isConnected());
    }
}