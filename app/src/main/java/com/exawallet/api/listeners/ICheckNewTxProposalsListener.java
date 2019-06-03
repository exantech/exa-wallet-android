package com.exawallet.api.listeners;

import com.exawallet.api.requests.TxProposalsRequest;

public interface ICheckNewTxProposalsListener extends IBaseSequenceListener {
    void onObtainRequest(TxProposalsRequest result);
}