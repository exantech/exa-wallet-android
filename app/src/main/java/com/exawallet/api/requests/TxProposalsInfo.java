package com.exawallet.api.requests;

import static com.exawallet.utils.Utils.containString;

public class TxProposalsInfo extends TxProposalsRequest {
    String[] approvals;
    String[] rejects;
    String last_signed_transaction;
    TxProposalsStatus status;

    protected TxProposalsInfo() {
        super();
    }

    public String[] getApprovals() {
        return approvals;
    }

    public String[] getRejects() {
        return rejects;
    }

    public String getLastSignedTransaction() {
        return last_signed_transaction;
    }

    public TxProposalsStatus getStatus() {
        return status;
    }

    public boolean hasDecision(String publicMultisigSignerKey) {
        return containString(approvals, publicMultisigSignerKey) || containString(rejects, publicMultisigSignerKey);
    }
}