package com.exawallet.api.listeners;

import com.exawallet.api.requests.TxProposalsInfo;

public interface IObtainTxProposalsInfoListener extends IBaseSequenceListener {
    void onObtainTxProposalsInfo(TxProposalsInfo[] result);
}