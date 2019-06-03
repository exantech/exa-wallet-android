package com.exawallet.presenters;

import static com.exawallet.model.meta.SharedMeta.buildJoined;
import static com.exawallet.presenters.operations.CreateWalletOperation.createWallet;
import static com.exawallet.utils.Screens.INVITE_CODE;
import static com.exawallet.utils.Screens.JOIN_SHARED_WALLET;

public class JoinSharedWalletPresenter extends CreateWalletPresenter {
    private final String mInviteCode;

    public JoinSharedWalletPresenter(String inviteCode) {
        this.mInviteCode = inviteCode;
    }

    protected void buildWallet(String password, String walletName, String node) {
        JOIN_SHARED_WALLET.pushObject(INVITE_CODE, mInviteCode);
        createWallet(password, walletName, node, buildJoined(mInviteCode), mWalletColor, mLanguage, mActivity);
    }
}