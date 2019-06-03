package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.model.Wallet;
import com.exawallet.model.WalletColor;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.presenters.results.WalletResult;
import com.exawallet.utils.HLog;

import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.WalletManager.walletManager;

public class EditWalletOperation extends WalletOperation {
    private final String mWalletName;
    private final String mNode;
    private final WalletColor mWalletColor;

    private EditWalletOperation(String walletName, String node, WalletColor walletColor, RootActivity activity) {
        super(activity);
        mWalletName = walletName;
        mNode = node;
        mWalletColor = walletColor;
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
            WalletMeta walletMeta = wallet.getWalletMeta();

            walletMeta.setColor(mWalletColor);
            walletMeta.setName(mWalletName);
            walletMeta.setDaemon(mNode);

            walletMeta.store();

            walletManager().setDaemon(walletMeta);

            return new WalletResult(wallet);
        } catch (Exception e) {
            return new WalletResult(e);
        }
    }

    public static void editWallet(String walletName, String node, WalletColor walletColor, RootActivity activity) {
        ENGINE.submit(new EditWalletOperation(walletName, node, walletColor, activity));
    }
}