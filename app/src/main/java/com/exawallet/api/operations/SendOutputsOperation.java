package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.ISendOutputsListener;
import com.exawallet.api.requests.Output;
import com.exawallet.api.results.APIResult;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SendOutputsOperation extends AuthorizedJsonEmptyResponseAPIOperation<Output, ISendOutputsListener> {
    private static final String OUTPUTS = "outputs";
    private final String mOutputs;

    private SendOutputsOperation(ISendOutputsListener listener, String outputs, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mOutputs = outputs;
    }

    @Override
    Output getObject() {
        return new Output(mOutputs);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(OUTPUTS);
    }

    @Override
    protected void onSuccess(APIResult<Object> result) {
        mListener.onSendOutputs();
    }

    public static void sendOutputs(ISendOutputsListener listener, String outputs, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new SendOutputsOperation(listener, outputs, sessionId, nonce, secretKey, signer).buildRequest());
    }
}