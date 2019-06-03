package com.exawallet.views;

public interface IWalletManageView extends IBaseView, ISynchronizationView {
    void setMnemonic(boolean hasSeed);
    void setChangeMode(boolean isDebugMode, boolean isSharedReady);
}