package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.ISendPublicKeyListener;
import com.exawallet.api.requests.PublicKey;
import com.exawallet.api.results.APIResult;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SendPublicKeyOperation extends AuthorizedJsonEmptyResponseAPIOperation<PublicKey, ISendPublicKeyListener> {
    private static final String CHANGE_PUBLIC_KEY = "change_public_key";
    private final String mPublicKey;

    private SendPublicKeyOperation(ISendPublicKeyListener listener, String publicKey, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mPublicKey = publicKey;
    }

    @Override
    PublicKey getObject() {
        return new PublicKey(mPublicKey);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(CHANGE_PUBLIC_KEY);
    }

    @Override
    protected void onSuccess(APIResult<Object> result) {
        mListener.onSendPublicKey();
    }

    public static void sendPublicKey(ISendPublicKeyListener listener, String publicKey, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new SendPublicKeyOperation(listener, publicKey, sessionId, nonce, secretKey, signer).buildRequest());
    }
}