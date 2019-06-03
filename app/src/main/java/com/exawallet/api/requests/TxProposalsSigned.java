package com.exawallet.api.requests;

public class TxProposalsSigned extends TxProposalsBase {
    String signed_transaction;

    public TxProposalsSigned(String destination_address, String description, long amount, long fee, String signedTransaction) {
        super(destination_address, description, amount, fee);
        this.signed_transaction = signedTransaction;
    }
}