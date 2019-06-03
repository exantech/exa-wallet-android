package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.model.WalletColor;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.presenters.results.WalletResult;
import com.exawallet.utils.HLog;

import java.util.Date;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.model.WalletStatus.CONFIRMED;
import static com.exawallet.model.meta.SharedMeta.buildRestored;
import static com.exawallet.presenters.operations.QRCodeOperation.generateQRCode;
import static com.exawallet.sequences.RestoreSharedWalletSequence.restoreSharedWallet;

public class RestoreWalletOperation extends WalletOperation {
    private final WalletColor mWalletColor;
    private final long mBlockHeight;
    private final String mPassword;
    private final String mMnemonic;
    private final String mWalletName;
    private final String mLanguage;
    private final String mNode;

    private RestoreWalletOperation(RootActivity activity, String mnemonic, long blockHeight, String password, String walletName, String node, WalletColor walletColor, String language) {
        super(activity);
        this.mMnemonic = mnemonic;
        this.mBlockHeight = blockHeight;
        this.mPassword = password;
        this.mWalletName = walletName;
        this.mNode = node;
        this.mWalletColor = walletColor;
        this.mLanguage = language;
    }

    @Override
    protected void onSuccess(WalletResult result) {
        HLog.debug(TAG, "onSuccess");
        String address = result.getResult().getAddress();
        generateQRCode(mActivity, address, qrResult -> walletManager().setQRAddress(qrResult.getResult()));

        restoreSharedWallet(mActivity, address);
    }

    @Override
    protected WalletResult execute() {
        try {
            return new WalletResult(walletManager().recoveryWallet(new WalletMeta(mActivity, mWalletName, buildRestored(), mWalletColor, isEmpty(mPassword), mLanguage, CONFIRMED, mNode, APP_CONTEXT.getNetworkType(), mBlockHeight, new Date()), mMnemonic, mBlockHeight, mPassword));
        } catch (Exception exception) {
            return new WalletResult(exception);
        }
    }

    public static void restoreWallet(String mnemonic, long blockHeight, String password, String walletName, String node, WalletColor walletColor, RootActivity activity, String language) {
        ENGINE.submit(new RestoreWalletOperation(activity, mnemonic, blockHeight, password, walletName, node, walletColor, language));
    }
}