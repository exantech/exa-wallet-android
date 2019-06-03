package com.exawallet.sequences;

import com.exawallet.RootActivity;
import com.exawallet.api.listeners.IBaseSequenceListener;
import com.exawallet.model.Wallet;
import com.exawallet.model.meta.CreatingMeta;
import com.exawallet.model.meta.SharedMeta;
import com.exawallet.model.meta.WalletMeta;
import org.greenrobot.eventbus.EventBus;

import static com.exawallet.model.WalletManager.walletManager;

public abstract class Sequence implements IBaseSequenceListener {
    static String TAG = Sequence.class.getSimpleName();

    final RootActivity mActivity;

    final EventBus mEventBus = EventBus.getDefault();

    Sequence(RootActivity activity) {
        this.mActivity = activity;
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public void onFailure(Exception exception) {
    }

    RootActivity getActivity() {
        return mActivity;
    }

    static void checkWallet(String address, Executor safeExecutor) {
        Wallet wallet = walletManager().getWallet();

        if (null != wallet) {
            WalletMeta walletMeta = wallet.getWalletMeta();
            if (address.equals(walletMeta.getAddress())) {
                SharedMeta sharedMeta = walletMeta.getSharedMeta();
                if (null != sharedMeta) {
                    safeExecutor.call(wallet, walletMeta, sharedMeta, sharedMeta.getCreatingMeta());
                }
            }
        }
    }

    void post(Object event) {
        mEventBus.post(event);
    }

    interface Executor {
        void call(Wallet wallet, WalletMeta walletMeta, SharedMeta sharedMeta, CreatingMeta creatingMeta);
    }
}