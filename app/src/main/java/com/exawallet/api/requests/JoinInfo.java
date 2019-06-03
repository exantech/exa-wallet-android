package com.exawallet.api.requests;

public class JoinInfo {
    final String invite_code;
    final String device_uid;
    final String multisig_info;

    public JoinInfo(String inviteCode, String deviceUid, String multisigInfo) {
        this.invite_code = inviteCode;
        this.device_uid = deviceUid;
        this.multisig_info = multisigInfo;
    }
}