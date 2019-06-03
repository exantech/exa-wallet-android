package com.exawallet.api.operations;

import com.android.volley.Request;
import com.exawallet.api.ISigner;
import com.exawallet.api.SignedJsonRequest;
import com.exawallet.api.listeners.IBaseSequenceListener;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public abstract class AuthorizedJsonAPIOperation<V, O, L extends IBaseSequenceListener> extends JsonAPIOperation<V, O, L> {
    private final String mSessionId;
    private final int mNonce;
    private final ISigner mSigner;
    private final String mSecretKey;

    AuthorizedJsonAPIOperation(L listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener);
        this.mSessionId = sessionId;
        this.mNonce = nonce;
        this.mSecretKey = secretKey;
        this.mSigner = signer;
    }

    @Override
    public final Request buildRequest() {
        return new SignedJsonRequest(getMethod(), getUrl(), getBody(), this, this, mSessionId, mNonce, mSecretKey, mSigner);
    }

    int getMethod() {
        return null == getBody() ? GET : POST;
    }
}