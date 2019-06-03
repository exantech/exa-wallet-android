package com.exawallet.api.listeners;

public interface ISendTxRelayStatusListener extends IBaseSequenceListener {
    void onSendRelayStatus(String proposalId);
}