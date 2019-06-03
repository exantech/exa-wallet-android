package com.exawallet.api.operations;

import com.exawallet.api.listeners.IObtainTickerListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.HitBtcTicker;

import static com.android.volley.Request.Method.GET;

public class ObtainTickerOperation extends StringAPIOperation<HitBtcTicker, IObtainTickerListener> {
    private String mTicker;

    private ObtainTickerOperation(String ticker, IObtainTickerListener listener) {
        super(listener);
        mTicker = ticker;
    }

    @Override
    Class<HitBtcTicker> getClassOfValue() {
        return HitBtcTicker.class;
    }

    @Override
    protected String getUrl() {
        return "https://api.hitbtc.com/api/2/public/ticker/".concat(getTicker());
    }

    private String getTicker() {
        return mTicker;
    }

    @Override
    protected void onSuccess(APIResult<HitBtcTicker> result) {
        mListener.onObtainTicker(result.getResult());
    }

    @Override
    int getMethod() {
        return GET;
    }

    public static void obtainTicker(String ticker, IObtainTickerListener listener) {
        putRequest(new ObtainTickerOperation(ticker, listener).buildRequest());
    }
}