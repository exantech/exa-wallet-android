package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IObtainMultisigInfoListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.MultisigInfos;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class ObtainMultisigInfoOperation extends AuthorizedStringAPIOperation<MultisigInfos, IObtainMultisigInfoListener> {
    private static final String INFO_MULTISIG = "info/multisig";

    private ObtainMultisigInfoOperation(IObtainMultisigInfoListener listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(INFO_MULTISIG);
    }

    @Override
    Class<MultisigInfos> getClassOfValue() {
        return MultisigInfos.class;
    }

    @Override
    protected void onSuccess(APIResult<MultisigInfos> result) {
        mListener.onObtainMultisigInfos(result.getResult());
    }

    public static void obtainMultisigInfo(IObtainMultisigInfoListener listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new ObtainMultisigInfoOperation(listener, sessionId, nonce, secretKey, signer).buildRequest());
    }
}