package com.exawallet.api;

import android.support.annotation.Nullable;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Response.error;
import static com.android.volley.Response.success;
import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;
import static java.lang.String.valueOf;

public class SignedStringRequest extends StringRequest {
    private final String mSessionId;
    private final int mNonce;
    private final String mSignum;

    /**
     * Creates a new request.
     * @param method
     * @param url           URL to fetch the JSON from
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     * @param secretKey
     */
    public SignedStringRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(method, url, listener, errorListener);
        this.mSessionId = sessionId;
        this.mNonce = nonce;
        this.mSignum = signer.signMessage(getMessage(sessionId, mNonce), secretKey);
    }

    @Override
    public final Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("X-Session-Id", mSessionId);
        params.put("X-Nonce", valueOf(mNonce));
        params.put("X-Signature", mSignum);
        return params;
    }

    private String getMessage(String sessionId, int nonce) {
        return sessionId.concat(valueOf(nonce));
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // The magic of the mute request happens here
        if (null == response.data || 0 == response.data.length) {
            if (response.statusCode >= 200 && response.statusCode <= 299) {
                // If the status is correct, we return a success but with a null object, because the server didn't return anything
                return success(null, parseCacheHeaders(response));
            } else {
                return error(new ParseError(response));
            }
        } else {
            return super.parseNetworkResponse(response);
        }
    }
}