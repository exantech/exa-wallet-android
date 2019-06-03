package com.exawallet.api;

import android.content.Context;
import android.support.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public enum APIManager {
    API;
    private RequestQueue mQueue;

    public void init(Context context) {
        mQueue = newRequestQueue(context);
    }

    public void putRequest(@NonNull final Request<Object> request) {
        mQueue.add(request);
    }
}
