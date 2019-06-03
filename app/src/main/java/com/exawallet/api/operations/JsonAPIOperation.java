package com.exawallet.api.operations;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.exawallet.api.listeners.IBaseSequenceListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.utils.HLog;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonAPIOperation<V, O, L extends IBaseSequenceListener> extends APIOperation<APIResult<V>, JSONObject, L> {
    JsonAPIOperation(L listener) {
        super(listener);
    }

    @Override
    protected final APIResult<V> buildResult(VolleyError volleyError) {
        return new APIResult<>(volleyError);
    }

    protected final APIResult<V> buildResult(JSONObject response) {
        return new APIResult<>(null == response || 0 == response.length() ? null : GSON.fromJson(response.toString(), getClassOfValue()));
    }

    @Override
    public Request buildRequest() {
        try {
            return new JsonObjectRequest(getUrl(), new JSONObject(getBody()), this, this);
        } catch (JSONException exception) {
            HLog.debug(TAG, exception.getMessage());
            onFailure(new APIResult<>(exception));
            return null;
        }
    }

    final String getBody() {
        return GSON.toJson(getObject());
    }

    abstract Class<V> getClassOfValue();

    abstract O getObject();
}