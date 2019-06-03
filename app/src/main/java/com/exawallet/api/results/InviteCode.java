package com.exawallet.api.results;

public class InviteCode {
    String invite_code;

    public InviteCode() {
    }

    public InviteCode(String inviteCode) {
        invite_code = inviteCode;
    }

    public String getInviteCode() {
        return invite_code;
    }
}