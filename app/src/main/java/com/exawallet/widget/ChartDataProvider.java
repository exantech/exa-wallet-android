package com.exawallet.widget;

import com.exawallet.api.results.HitBtcCandle;

import java.util.Iterator;

public interface ChartDataProvider {
    float getMinValue();

    float getMaxValue();

    long getMinTime();

    long getMaxTime();

    Iterator<ChartData> getIterator();

    void setCandles(HitBtcCandle[] result);

    boolean isInited();
}