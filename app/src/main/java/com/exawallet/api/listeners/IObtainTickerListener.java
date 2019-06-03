package com.exawallet.api.listeners;

import com.exawallet.api.results.HitBtcTicker;

public interface IObtainTickerListener extends IBaseSequenceListener {
    void onObtainTicker(HitBtcTicker result);
}
