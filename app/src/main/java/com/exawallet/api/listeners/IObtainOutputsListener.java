package com.exawallet.api.listeners;

import com.exawallet.api.results.Outputs;

public interface IObtainOutputsListener extends IBaseSequenceListener {
    void onObtainOutputs(Outputs result);
}