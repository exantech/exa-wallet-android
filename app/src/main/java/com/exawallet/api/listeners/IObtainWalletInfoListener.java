package com.exawallet.api.listeners;

import com.exawallet.api.results.WalletInfo;

public interface IObtainWalletInfoListener extends IBaseSequenceListener {
    void onObtainWalletInfo(WalletInfo walletInfo);
}