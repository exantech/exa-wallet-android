package com.exawallet.api.listeners;

import com.exawallet.api.results.OutputsProcessed;

public interface IObtainOutputsRequestListener extends IBaseSequenceListener {
    void onObtainOutputsRequest(OutputsProcessed result);
}