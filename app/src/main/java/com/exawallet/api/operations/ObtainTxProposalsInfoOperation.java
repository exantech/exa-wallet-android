package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IObtainTxProposalsInfoListener;
import com.exawallet.api.requests.TxProposalsInfo;
import com.exawallet.api.results.APIResult;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class ObtainTxProposalsInfoOperation extends AuthorizedStringAPIOperation<TxProposalsInfo[], IObtainTxProposalsInfoListener> {
    private static final String TX_PROPOSALS = "tx_proposals";

    private ObtainTxProposalsInfoOperation(IObtainTxProposalsInfoListener listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(TX_PROPOSALS);
    }

    @Override
    Class<TxProposalsInfo[]> getClassOfValue() {
        return TxProposalsInfo[].class;
    }

    @Override
    protected void onSuccess(APIResult<TxProposalsInfo[]> result) {
        mListener.onObtainTxProposalsInfo(result.getResult());
    }

    public static void obtainTxProposalsInfo(IObtainTxProposalsInfoListener listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new ObtainTxProposalsInfoOperation(listener, sessionId, nonce, secretKey, signer).buildRequest());
    }
}