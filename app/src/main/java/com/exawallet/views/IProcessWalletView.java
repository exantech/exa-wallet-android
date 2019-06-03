package com.exawallet.views;

import com.exawallet.model.WalletColor;

public interface IProcessWalletView extends IBaseView{
    void checkWalletName(boolean matches);

    void setWalletColor(WalletColor walletColor);
}
