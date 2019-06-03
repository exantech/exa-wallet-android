package com.exawallet.presenters;

import com.exawallet.common.WalletNewBlock;
import com.exawallet.fragments.SyncSplashFragment;
import com.exawallet.views.ISynchronizationView;
import org.greenrobot.eventbus.Subscribe;

import static com.exawallet.model.Wallet.ConnectionStatus.ConnectionStatus_Connected;
import static com.exawallet.model.WalletManager.walletManager;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class BaseSyncWalletPresenter<V extends ISynchronizationView> extends AttachedPresenter<V> {
    static final int MIN_BLOCKS = 20;
    static final int MIN_BACKGROUND_SYNC_BLOCKS = 50;

    @Override
    public void onResume() {
        super.onResume();

        if (0 == mWallet.getDaemonBlockChainHeight()) {
            mView.notifySynchronized(false);
            mWallet.refreshAsync();
        } else if (MIN_BLOCKS <= mWallet.getDaemonBlockChainHeight() - mWallet.getBlockChainHeight()) {
            mView.notifySynchronized(false);
        } else {
            mView.notifySynchronized(true);
            mWallet.startRefresh();
        }

        mView.notifyConnection(ConnectionStatus_Connected == walletManager().getConnectionStatus());
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(final WalletNewBlock event) {
        if (MIN_BACKGROUND_SYNC_BLOCKS < event.getRemaining()) {
            mActivity.show(SyncSplashFragment.newInstance());
        }
    }

    public void startSynchronization() {
        mWallet.startRefresh();
        mActivity.show(SyncSplashFragment.newInstance());
    }
}