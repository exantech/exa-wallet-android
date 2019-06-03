package com.exawallet.api.listeners;

import com.exawallet.api.requests.TxProposalsRequest;

public interface ITxProposalsRequestListener extends IBaseSequenceListener {
    void onTxProposalsRequest(TxProposalsRequest result);
}