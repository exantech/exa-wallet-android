package com.exawallet.api.requests;

public class MultisigInfo {
    final int participants;
    final int signers;
    final String name;
    final String device_uid;
    final String multisig_info;

    public MultisigInfo(int participants, int signers, String name, String device_uid, String multisigInfo) {
        super();
        this.participants = participants;
        this.signers = signers;
        this.name = name;
        this.device_uid = device_uid;
        this.multisig_info = multisigInfo;
    }
}