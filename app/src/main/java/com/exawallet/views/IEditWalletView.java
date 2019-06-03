package com.exawallet.views;

public interface IEditWalletView extends IProcessWalletView {
    void showWalletType(boolean isPersonal);

    void showWalletName(String name);

    void showWalletNode(String daemonAddress);
}