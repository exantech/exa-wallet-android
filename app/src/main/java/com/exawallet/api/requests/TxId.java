package com.exawallet.api.requests;

public class TxId {
    String[] tx_id;

    public String[] getTxId() {
        return tx_id;
    }

    public TxId() {
    }

    public TxId(String[] txId) {
        this.tx_id = txId;
    }
}