package com.exawallet.api.listeners;

import com.exawallet.api.results.InviteCode;

public interface ICreateMultisigListener extends IBaseSequenceListener {
    void onCreateMultisig(InviteCode result);
}