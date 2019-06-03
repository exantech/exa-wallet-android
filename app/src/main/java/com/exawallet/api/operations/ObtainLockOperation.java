package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IObtainLockListener;
import com.exawallet.api.results.APIResult;

import static com.android.volley.Request.Method.HEAD;
import static com.exawallet.common.AppContext.APP_CONTEXT;

public class ObtainLockOperation extends AuthorizedStringEmptyResponseAPIOperation<IObtainLockListener> {
    private static final String DECISION = "/decision";
    private static final String TX_PROPOSALS = "tx_proposals/";

    private final String mPropsalId;

    private ObtainLockOperation(IObtainLockListener listener, String propsalId, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        mPropsalId = propsalId;
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(TX_PROPOSALS).concat(mPropsalId).concat(DECISION);
    }

    @Override
    int getMethod() {
        return HEAD;
    }

    @Override
    protected void onSuccess(APIResult<Object> result) {
        mListener.onLock();
    }

    public static void obtainLockInfo(IObtainLockListener listener, String propsalId, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new ObtainLockOperation(listener, propsalId, sessionId, nonce, secretKey, signer).buildRequest());
    }
}