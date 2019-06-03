package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.ISendExtraMultisigListener;
import com.exawallet.api.requests.ExtraMultisigInfo;
import com.exawallet.api.results.APIResult;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SendExtraMultisigOperation extends AuthorizedJsonEmptyResponseAPIOperation<ExtraMultisigInfo, ISendExtraMultisigListener> {
    private static final String EXTRA_MULTISIG_INFO = "extra_multisig_info";
    private final String mMultisigInfo;
    private final int mCounter;

    private SendExtraMultisigOperation(ISendExtraMultisigListener listener, String multisigInfo, int counter, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mMultisigInfo = multisigInfo;
        this.mCounter = counter;
    }

    @Override
    ExtraMultisigInfo getObject() {
        return new ExtraMultisigInfo(mMultisigInfo);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(EXTRA_MULTISIG_INFO)
                + (0 < mCounter ? "/" + mCounter : "")
                ;
    }

    @Override
    protected void onSuccess(APIResult<Object> result) {
        mListener.onSendExtraMultisig();
    }

    public static void sendExtraMultisigInfo(ISendExtraMultisigListener listener, String multisigInfo, int counter, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new SendExtraMultisigOperation(listener, multisigInfo, counter, sessionId, nonce, secretKey, signer).buildRequest());
    }
}