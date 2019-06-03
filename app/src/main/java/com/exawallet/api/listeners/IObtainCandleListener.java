package com.exawallet.api.listeners;

import com.exawallet.api.results.HitBtcCandle;

public interface IObtainCandleListener extends IBaseSequenceListener {
    void onObtainCandles(HitBtcCandle[] result);
}