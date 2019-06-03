package com.exawallet.api.requests;

import static android.text.TextUtils.isEmpty;

class TxProposalsBase {
    String destination_address;
    String description;
    long amount;
    long fee;

    TxProposalsBase() {
        super();
    }

    TxProposalsBase(String destination_address, String description, long amount, long fee) {
        super();
        this.destination_address = destination_address;
        this.description = description;
        this.amount = amount;
        this.fee = fee;
    }

    public String getDestinationAddress() {
        return destination_address;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public long getFee() {
        return fee;
    }

    public boolean isNotEmpty() {
        return isEmpty(destination_address);
    }
}