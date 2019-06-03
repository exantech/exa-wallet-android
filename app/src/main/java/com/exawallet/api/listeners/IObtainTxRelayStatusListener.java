package com.exawallet.api.listeners;

import com.exawallet.api.requests.TxRelayStatus;

public interface IObtainTxRelayStatusListener extends IBaseSequenceListener {
    void onObtainTxRelayStatus(TxRelayStatus result);
}