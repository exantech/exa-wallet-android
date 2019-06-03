package com.exawallet.api.requests;

public class TxProposalDecision {
    String signed_transaction;
    boolean approved;
    int approval_nonce;

    public TxProposalDecision(String signed_transaction, boolean approved, int approval_nonce) {
        this.signed_transaction = signed_transaction;
        this.approved = approved;
        this.approval_nonce = approval_nonce;
    }
}