package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IObtainOutputsListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.Outputs;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class ObtainOutputsOperation extends AuthorizedStringAPIOperation<Outputs, IObtainOutputsListener> {
    private static final String OUTPUTS = "outputs";

    private ObtainOutputsOperation(IObtainOutputsListener listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(OUTPUTS);
    }

    @Override
    Class<Outputs> getClassOfValue() {
        return Outputs.class;
    }

    @Override
    protected void onSuccess(APIResult<Outputs> result) {
        mListener.onObtainOutputs(result.getResult());
    }

    public static void obtainOutputs(IObtainOutputsListener listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new ObtainOutputsOperation(listener, sessionId, nonce, secretKey, signer).buildRequest());
    }
}