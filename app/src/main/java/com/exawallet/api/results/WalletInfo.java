package com.exawallet.api.results;

public class WalletInfo {
    String status;
    int participants;
    int signers;
    int joined;
    int changed_keys;

    public String getStatus() {
        return status;
    }

    public int getParticipants() {
        return participants;
    }

    public int getSigners() {
        return signers;
    }

    public int getJoined() {
        return joined;
    }

    public int getChangedKeys() {
        return changed_keys;
    }
}