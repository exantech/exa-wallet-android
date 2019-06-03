package com.exawallet.presenters.utils;

import com.exawallet.api.results.HitBtcCandle;
import com.exawallet.widget.ChartData;
import com.exawallet.widget.ChartDataProvider;

import java.util.Arrays;
import java.util.Iterator;

public class ChartDataProviderImpl implements ChartDataProvider {
    public static final String XMRUSD = "xmrusd";
    static final String XMRBTC = "xmrbtc";
    static final String XMRETH = "xmreth";
    static final String XMRDAI = "xmrdai";
    static final String XMREURS = "xmreurs";
    static final String XMREOS = "xmreos";

    static final String M_1 = "M1";
    static final String M_3 = "M3";
    static final String M_5 = "M5";
    static final String M_15 = "M15";
    static final String M_30 = "M30";
    static final String H_1 = "H1";
    static final String H_4 = "H4";
    static final String D_1 = "D1";
    static final String D_7 = "D7";
    static final String MO_1 = "1M";
    public static final String XMR_TO_USD = "XMR to USD";
    public static final String XMR_TO_BTC = "XMR to BTC";
    public static final String XMR_TO_ETH = "XMR to ETH";
    public static final String XMR_TO_DAI = "XMR to DAI";
    public static final String XMR_TO_EURS = "XMR to EURS";
    public static final String XMR_TO_EOS = "XMR to EOS";

    private HitBtcCandle[] mCandles;
    private float mMinValue;
    private float mMaxValue;

    private String mTicker = XMRUSD;
    private String mPeriod = M_30;

    @Override
    public float getMinValue() {
        return mMinValue;
    }

    @Override
    public float getMaxValue() {
        return mMaxValue;
    }

    @Override
    public long getMinTime() {
        return mCandles[0].getTimestamp().getTime();
    }

    @Override
    public long getMaxTime() {
        return mCandles[mCandles.length - 1].getTimestamp().getTime();
    }

    @Override
    public Iterator<ChartData> getIterator() {
        return new Iterator<ChartData>() {
            Iterator<HitBtcCandle> iterator = Arrays.asList(mCandles).iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ChartData next() {
                HitBtcCandle candle = iterator.next();
                return new ChartData() {
                    @Override
                    public long getTime() {
                        return candle.getTimestamp().getTime();
                    }

                    @Override
                    public float getValue() {
                        return (float) candle.getClose();
                    }
                };
            }
        };
    }

    @Override
    public void setCandles(HitBtcCandle[] result) {
        mCandles = result;

        mMaxValue = Float.MIN_VALUE;
        mMinValue = Float.MAX_VALUE;

        for (HitBtcCandle candle : result) {
            mMinValue = (float) Math.min(candle.getClose(), mMinValue);
            mMaxValue = (float) Math.max(candle.getClose(), mMaxValue);
        }
    }

    @Override
    public boolean isInited() {
        return null != mCandles;
    }

    public String getTicker() {
        return mTicker;
    }

    public String getPeriod() {
        return mPeriod;
    }

    public String getXmrTicker() {
        return getTicker().toUpperCase().replace("XMR", "");
    }

    public String[] getTickers() {
        return new String[]{XMRUSD, XMRBTC, XMRETH, XMRDAI, XMREURS, XMREOS};
    }

    public String[] getTickerNames() {
        return new String[]{XMR_TO_USD, XMR_TO_BTC, XMR_TO_ETH, XMR_TO_DAI, XMR_TO_EURS, XMR_TO_EOS};
    }

    public String[] getPeriods() {
        return new String[]{M_1, M_3, M_5, M_15, M_30, H_1, H_4, D_1, D_7, MO_1};
    }

    public String[] getPeriodNames() {
        return new String[]{"1 min", "3 min", "5 min", "15 min", "30 min", "1 hour", "4 hours", "day", "week", "month"};
    }

    public void setTicker(String ticker) {
        mTicker = ticker;
    }

    public void setPeriod(String period) {
        mPeriod = period;
    }
}