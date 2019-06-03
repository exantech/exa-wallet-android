package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.model.Wallet;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.presenters.results.WalletResult;
import com.exawallet.utils.HLog;

import static com.exawallet.common.AppContext.setCurrentWalletAddress;
import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.presenters.operations.QRCodeOperation.generateQRCode;
import static com.exawallet.utils.Utils.selectWalletScreen;

public class OpenWalletOperation extends WalletOperation {
    private final String mPassword;
    private final WalletMeta mWalletMeta;

    private OpenWalletOperation(RootActivity activity, String password, WalletMeta walletMeta) {
        super(activity);
        this.mPassword = password;
        this.mWalletMeta = walletMeta;
    }

    @Override
    protected void onSuccess(WalletResult result) {
        HLog.debug(TAG, "onSuccess");
        Wallet wallet = result.getResult();
        generateQRCode(mActivity, wallet.getAddress(), qrResult -> walletManager().setQRAddress(qrResult.getResult()));

        selectWalletScreen(mWalletMeta, mActivity);
        setCurrentWalletAddress(mWalletMeta.getAddress());
    }

    @Override
    protected WalletResult execute() {
        try {
            return new WalletResult(walletManager().openWallet(mWalletMeta, mPassword));
        } catch (Exception e) {
            return new WalletResult(e);
        }
    }

    public static void openWallet(String password, WalletMeta walletMeta, RootActivity activity) {
        ENGINE.submit(new OpenWalletOperation(activity, password, walletMeta));
    }
}