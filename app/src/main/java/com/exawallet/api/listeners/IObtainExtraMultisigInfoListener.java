package com.exawallet.api.listeners;

import com.exawallet.api.results.ExtraMultisigKeys;

public interface IObtainExtraMultisigInfoListener extends IBaseSequenceListener {
    void onObtainExtraMultisigKeys(ExtraMultisigKeys result);
}