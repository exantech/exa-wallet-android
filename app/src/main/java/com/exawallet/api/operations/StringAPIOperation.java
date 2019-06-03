package com.exawallet.api.operations;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.exawallet.api.listeners.IBaseSequenceListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.utils.HLog;

import static android.text.TextUtils.isEmpty;
import static com.android.volley.Request.Method.POST;

public abstract class StringAPIOperation<V, L extends IBaseSequenceListener> extends APIOperation<APIResult<V>, String, L> {
    StringAPIOperation(L listener) {
        super(listener);
    }

    public Request buildRequest() {
        String url = getUrl();
        HLog.debug(TAG, "buildRequest " + url);
        return new StringRequest(getMethod(), url, this, this);
    }

    int getMethod() {
        return POST;
    }

    @Override
    protected final APIResult<V> buildResult(VolleyError volleyError) {
        return new APIResult<>(volleyError);
    }

    protected final APIResult<V> buildResult(String response) {
        return new APIResult<>(isEmpty(response) ? null : GSON.fromJson(response, getClassOfValue()));
    }

    abstract Class<V> getClassOfValue();
}