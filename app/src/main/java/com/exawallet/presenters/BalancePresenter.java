package com.exawallet.presenters;

import com.exawallet.api.listeners.IObtainTickerListener;
import com.exawallet.api.results.HitBtcTicker;
import com.exawallet.common.WalletNewBlock;
import com.exawallet.common.WalletRefreshed;
import com.exawallet.fragments.ChartFragment;
import com.exawallet.fragments.ReceiveFragment;
import com.exawallet.fragments.SendFragment;
import com.exawallet.fragments.WalletManageFragment;
import com.exawallet.model.Wallet;
import com.exawallet.model.meta.SharedMeta;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IBalanceView;
import org.greenrobot.eventbus.Subscribe;

import static com.exawallet.api.operations.ObtainTickerOperation.obtainTicker;
import static com.exawallet.presenters.utils.ChartDataProviderImpl.XMRUSD;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class BalancePresenter extends BaseSyncWalletPresenter<IBalanceView> implements IObtainTickerListener {
    @Override
    public void onResume() {
        super.onResume();
        mView.setWalletType(null == mWalletMeta.getSharedMeta() ? R.string.personal_wallet : R.string.shared_wallet);
        mView.showSync(mWallet.getBlockChainHeight());

        update(mWallet);
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(final WalletRefreshed event) {
        update(mWallet);
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(final WalletNewBlock event) {
        mView.showSync(mWallet.getBlockChainHeight());
    }

    private void update(final Wallet wallet) {
        SharedMeta sharedMeta = wallet.getWalletMeta().getSharedMeta();

        mView.setSyncStatus(null != sharedMeta && sharedMeta.needSendOutput() ? R.drawable.ic_restore_fab : null != sharedMeta && sharedMeta.needRequestOutputs() ? R.drawable.ic_personal_fab : 0);

        mView.showBalance(wallet.getUnlockedBalance());
        mView.showBalanceUnconfirmed(wallet.getUnconfirmedBalance());
        mView.showTransactions(wallet.getHistory().getAll(wallet));

        mView.showTxProposals(null != sharedMeta, null != sharedMeta ? sharedMeta.getTxProposalMetas() : null);

        obtainTicker(XMRUSD, this);
    }

    public void onManage() {
        mActivity.show(WalletManageFragment.newInstance());
    }

    public void onChart() {
        mActivity.show(ChartFragment.newInstance());
    }

    public void onSend() {
        mActivity.show(SendFragment.newInstance());
    }

    public void onReceive() {
        mActivity.show(ReceiveFragment.newInstance());
    }

    @Override
    public void onObtainTicker(HitBtcTicker result) {
        mView.showBalanceUsd((0 != mWallet.getUnlockedBalance() ? mWallet.getUnlockedBalance() : mWallet.getUnconfirmedBalance()) * result.getLast());
    }

    @Override
    public void onFailure(Exception exception) {
    }
}