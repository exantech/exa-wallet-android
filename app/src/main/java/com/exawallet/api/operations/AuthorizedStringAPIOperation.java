package com.exawallet.api.operations;

import com.android.volley.Request;
import com.exawallet.api.ISigner;
import com.exawallet.api.SignedStringRequest;
import com.exawallet.api.listeners.IBaseSequenceListener;

import static com.android.volley.Request.Method.GET;

abstract class AuthorizedStringAPIOperation<V, L extends IBaseSequenceListener> extends StringAPIOperation<V, L> {
    private final String mSessionId;
    private final int mNonce;
    private final ISigner mSigner;
    private final String mSecretKey;

    AuthorizedStringAPIOperation(L listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener);
        this.mSessionId = sessionId;
        this.mNonce = nonce;
        this.mSecretKey = secretKey;
        this.mSigner = signer;
    }
    @Override
    public final Request buildRequest() {
        return new SignedStringRequest(getMethod(), getUrl(), this, this, mSessionId, mNonce, mSecretKey, mSigner);
    }

    int getMethod() {
        return GET;
    }
}