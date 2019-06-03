package com.exawallet.views;

import com.exawallet.model.meta.TxProposalsMeta;

public interface ITxProposalsMetaView extends ISynchronizationView {
    void showTxProposalsMeta(TxProposalsMeta txProposalsMeta);

    void enableButtons(boolean enable);
}
