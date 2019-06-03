package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IObtainExtraMultisigInfoListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.ExtraMultisigKeys;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class ObtainExtraMultisigInfoOperation extends AuthorizedStringAPIOperation<ExtraMultisigKeys, IObtainExtraMultisigInfoListener> {
    private static final String INFO_EXTRA_MULTISIG = "info/extra_multisig";
    private final int mCounter;

    private ObtainExtraMultisigInfoOperation(IObtainExtraMultisigInfoListener listener, int counter, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mCounter = counter;
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(INFO_EXTRA_MULTISIG)
                + (0 < mCounter ? "/" + mCounter : "")
                ;
    }

    @Override
    Class<ExtraMultisigKeys> getClassOfValue() {
        return ExtraMultisigKeys.class;
    }

    @Override
    protected void onSuccess(APIResult<ExtraMultisigKeys> result) {
        mListener.onObtainExtraMultisigKeys(result.getResult());
    }

    public static void obtainExtraMultisigInfo(IObtainExtraMultisigInfoListener listener, int counter, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new ObtainExtraMultisigInfoOperation(listener, counter, sessionId, nonce, secretKey, signer).buildRequest());
    }
}