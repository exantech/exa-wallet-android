package com.exawallet.api.results;

public class HitBtcTicker extends HitBtcData {
    double ask;
    double bid;
    double last;
    double low;
    double high;
    String symbol;

    public double getAsk() {
        return ask;
    }

    public double getBid() {
        return bid;
    }

    public double getLast() {
        return last;
    }

    public double getLow() {
        return low;
    }

    public double getHigh() {
        return high;
    }

    public String getSymbol() {
        return symbol;
    }
}