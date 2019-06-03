package com.exawallet.api.operations;

import com.exawallet.api.listeners.IOpenSessionListener;
import com.exawallet.api.requests.PublicKey;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.SessionId;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class OpenSessionOperation extends JsonAPIOperation<SessionId, PublicKey, IOpenSessionListener> {
    private static final String OPEN_SESSION = "open_session";
    private final String mPublicKey;

    private OpenSessionOperation(IOpenSessionListener listener, String publicKey) {
        super(listener);
        mPublicKey = publicKey;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(OPEN_SESSION);
    }

    @Override
    Class<SessionId> getClassOfValue() {
        return SessionId.class;
    }

    @Override
    PublicKey getObject() {
        return new PublicKey(mPublicKey);
    }

    @Override
    protected void onSuccess(APIResult<SessionId> result) {
        mListener.onOpenSession(result.getResult());
    }

    public static void openSession(IOpenSessionListener listener, String publicKey) {
        putRequest(new OpenSessionOperation(listener, publicKey).buildRequest());
    }
}