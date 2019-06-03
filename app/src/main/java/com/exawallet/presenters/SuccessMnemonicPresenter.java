package com.exawallet.presenters;

import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.WALLETS;
import static com.exawallet.utils.Utils.selectWalletScreen;

public class SuccessMnemonicPresenter extends AttachedPresenter<IBaseView> {
    public void onContinueButton() {
        BACK_PATH.clear();
        BACK_PATH.setScreen(WALLETS);
        selectWalletScreen(mWalletMeta, mActivity);
    }
}