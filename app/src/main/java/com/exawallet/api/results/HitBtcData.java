package com.exawallet.api.results;

import java.util.Date;

public class HitBtcData {
    double open;
    double volume;
    double volumeQuote;
    Date timestamp;

    public double getOpen() {
        return open;
    }

    public double getVolume() {
        return volume;
    }

    public double getVolumeQuote() {
        return volumeQuote;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}