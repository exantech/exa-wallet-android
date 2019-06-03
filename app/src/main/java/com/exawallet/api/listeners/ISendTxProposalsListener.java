package com.exawallet.api.listeners;

import com.exawallet.api.results.TxProposalsId;
import com.exawallet.model.meta.TxProposalsMeta;

public interface ISendTxProposalsListener extends IBaseSequenceListener {
    void onSendProposal(TxProposalsId result, TxProposalsMeta mTxProposalsMeta);
}