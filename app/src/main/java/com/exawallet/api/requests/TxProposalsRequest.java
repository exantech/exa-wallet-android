package com.exawallet.api.requests;

public class TxProposalsRequest extends TxProposalsBase {
    String proposal_id;

    TxProposalsRequest(){
        super();
    }

    public String getProposalId() {
        return proposal_id;
    }
}