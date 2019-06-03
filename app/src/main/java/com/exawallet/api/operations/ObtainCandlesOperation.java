package com.exawallet.api.operations;

import com.exawallet.api.listeners.IObtainCandleListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.HitBtcCandle;

import static com.android.volley.Request.Method.GET;

public class ObtainCandlesOperation extends StringAPIOperation<HitBtcCandle[], IObtainCandleListener> {
    private String mTicker;
    private String mPeriod;

    private ObtainCandlesOperation(String ticker, String period, IObtainCandleListener listener) {
        super(listener);
        mTicker = ticker;
        this.mPeriod = period;
    }

    @Override
    Class getClassOfValue() {
        return HitBtcCandle[].class;
    }

    @Override
    protected String getUrl() {
        return "https://api.hitbtc.com/api/2/public/candles/".concat(getTicker()).concat("?period=").concat(getPeroid());
    }

    private String getPeroid() {
        return mPeriod;
    }

    private String getTicker() {
        return mTicker;
    }

    @Override
    protected void onSuccess(APIResult<HitBtcCandle[]> result) {
        mListener.onObtainCandles(result.getResult());
    }

    @Override
    int getMethod() {
        return GET;
    }

    public static void obtainCandles(String ticker, String period, IObtainCandleListener listener) {
        putRequest(new ObtainCandlesOperation(ticker, period, listener).buildRequest());
    }
}