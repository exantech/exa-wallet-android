package com.exawallet.views;

public interface ICreateSharedWalletView extends IRecreateWalletView {
    void fixParticipants(int participants);

    void fixSigners(int signers);
}