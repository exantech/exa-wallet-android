package com.exawallet.presenters;

import com.exawallet.model.Wallet;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.views.IEditWalletView;

import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.presenters.operations.EditWalletOperation.editWallet;

public class EditWalletPresenter extends ProcessWalletPresenter<IEditWalletView> {
    @Override
    public void onResume() {
        super.onResume();

        Wallet wallet = walletManager().getWallet();
        WalletMeta walletMeta = wallet.getWalletMeta();
        mWalletColor = walletMeta.getColor();

        mView.showWalletType(null == walletMeta.getSharedMeta());
        mView.setWalletColor(walletMeta.getColor());

        mView.showWalletName(walletMeta.getName());
        mView.showWalletNode(walletMeta.getDaemonAddress());
    }

    public void onEditWallet(String walletName, String node) {
        editWallet(walletName, node, mWalletColor, mActivity);
    }
}