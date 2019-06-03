package com.exawallet.api.requests;

public class TxRelayStatus {
    String proposal_id;
    boolean sent;
    String transaction_hash;

    public String getProposalId() {
        return proposal_id;
    }

    public boolean isSent() {
        return sent;
    }

    public String getTransactionHash() {
        return transaction_hash;
    }
}