package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.fragments.ShowMnemonicFragment;
import com.exawallet.model.WalletColor;
import com.exawallet.model.meta.SharedMeta;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.presenters.results.WalletResult;
import com.exawallet.utils.HLog;

import java.util.Date;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.common.AppContext.setCurrentWalletAddress;
import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.model.WalletStatus.UNCONFIRMED;
import static com.exawallet.presenters.operations.QRCodeOperation.generateQRCode;

public class CreateWalletOperation extends WalletOperation {
    private final String mPassword;
    private final String mWalletName;
    private final WalletColor mWalletColor;
    private final String mLanguage;
    private final String mNode;
    private final SharedMeta mSharedMeta;

    private CreateWalletOperation(String password, String walletName, String node, SharedMeta sharedMeta, WalletColor walletColor, String language, RootActivity activity) {
        super(activity);
        this.mPassword = password;
        this.mWalletName = walletName;
        this.mNode = node;
        this.mSharedMeta = sharedMeta;
        this.mWalletColor = walletColor;
        this.mLanguage = language;
    }

    @Override
    protected void onSuccess(WalletResult result) {
        HLog.debug(TAG, "onSuccess");
        generateQRCode(mActivity, result.getResult().getAddress(), qrResult -> walletManager().setQRAddress(qrResult.getResult()));
        mActivity.show(ShowMnemonicFragment.newInstance());
        setCurrentWalletAddress(result.getResult().getAddress());
    }

    @Override
    protected WalletResult execute() {
        try {
            return new WalletResult(walletManager().createWallet(new WalletMeta(mActivity, mWalletName, mSharedMeta, mWalletColor, isEmpty(mPassword),  mLanguage, UNCONFIRMED, mNode, APP_CONTEXT.getNetworkType(), 0, new Date()), mPassword));
        } catch (Exception e) {
            return new WalletResult(e);
        }
    }

    public static void createWallet(String password, String walletName, String node, WalletColor walletColor, String language, RootActivity activity) {
        ENGINE.submit(new CreateWalletOperation(password, walletName, node, null, walletColor, language, activity));
    }

    public static void createWallet(String password, String walletName, String node, SharedMeta sharedMeta, WalletColor walletColor, String language, RootActivity activity) {
        ENGINE.submit(new CreateWalletOperation(password, walletName, node, sharedMeta, walletColor, language, activity));
    }
}