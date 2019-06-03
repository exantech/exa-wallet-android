package com.exawallet.api.operations;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.exawallet.api.listeners.IBaseSequenceListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.engine.operations.Operation;
import com.exawallet.utils.HLog;
import com.google.gson.Gson;

import static com.exawallet.api.APIManager.API;

public abstract class APIOperation<R extends APIResult, T, L extends IBaseSequenceListener> extends Operation<R> implements Response.Listener<T>, Response.ErrorListener {
    static final Gson GSON = new Gson();

    final L mListener;

    APIOperation(L mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        onFailure(buildResult(volleyError));
    }

    @Override
    public void onResponse(T response) {
        HLog.debug(TAG, "onResponse " + response);
        R result = buildResult(response);
        if (result.isSuccess()) {
            onSuccess(result);
        } else {
            onFailure(result);
        }
    }

    protected abstract String getUrl();

    protected abstract R buildResult(VolleyError volleyError);

    protected abstract R buildResult(T response);

    public abstract Request buildRequest();

    @Override
    protected void onStart() {
    }

    @Override
    protected void onFailure(R result) {
        HLog.debug(TAG, "onFailure");
        mListener.onFailure(result.getException());
    }

    static void putRequest(Request request) {
        API.putRequest(request);
    }
}