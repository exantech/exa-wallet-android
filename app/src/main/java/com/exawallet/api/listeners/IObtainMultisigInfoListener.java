package com.exawallet.api.listeners;

import com.exawallet.api.results.MultisigInfos;

public interface IObtainMultisigInfoListener extends IBaseSequenceListener {
    void onObtainMultisigInfos(MultisigInfos result);
}