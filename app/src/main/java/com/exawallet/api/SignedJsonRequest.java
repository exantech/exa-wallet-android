package com.exawallet.api;

import android.support.annotation.Nullable;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Response.error;
import static com.android.volley.Response.success;
import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;
import static com.android.volley.toolbox.HttpHeaderParser.parseCharset;
import static java.lang.String.valueOf;

public class SignedJsonRequest extends JsonRequest<JSONObject> {
    private final String mSessionId;
    private final int mNonce;
    private final String mSignum;

    /**
     * Creates a new request.
     * @param method
     * @param url           URL to fetch the JSON from
     * @param jsonRequest   A {@link JSONObject} to post with the request. Null indicates no
*                      parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     * @param secretKey
     */
    public SignedJsonRequest(int method, String url, @Nullable String jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(method, url, jsonRequest, listener, errorListener);
        this.mSessionId = sessionId;
        this.mNonce = nonce;
        this.mSignum = signer.signMessage(getMessage(jsonRequest, sessionId, mNonce), secretKey);
    }

    @Override
    public final Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json; charset=UTF-8");
        params.put("X-Session-Id", mSessionId);
        params.put("X-Nonce", valueOf(mNonce));
        params.put("X-Signature", mSignum);
        return params;
    }

    private String getMessage(String jsonRequest, String sessionId, int nonce) {
        return jsonRequest.concat(sessionId).concat(valueOf(nonce));
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        // The magic of the mute request happens here
        if (null == response.data || 0 == response.data.length) {
            if (response.statusCode >= 200 && response.statusCode <= 299) {
                // If the status is correct, we return a success but with a null object, because the server didn't return anything
                return success(null, parseCacheHeaders(response));
            } else {
                return error(new ParseError(response));
            }
        } else {
            try {
                return success(new JSONObject(new String(response.data, parseCharset(response.headers, PROTOCOL_CHARSET))), parseCacheHeaders(response));
            } catch (UnsupportedEncodingException | JSONException e) {
                return error(new ParseError(e));
            }
        }
    }
}