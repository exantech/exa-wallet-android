package com.exawallet.api.operations;

import com.android.volley.VolleyError;
import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.ISendTxRelayStatusListener;
import com.exawallet.api.requests.TxId;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.SignedTransaction;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SendTxRelayStatusOperation extends AuthorizedJsonAPIOperation<SignedTransaction, TxId, ISendTxRelayStatusListener> {
    private static final String TX_RELAY_STATUS = "tx_relay_status/";
    private final TxId mTxId;
    private final String mProposalId;


    private SendTxRelayStatusOperation(ISendTxRelayStatusListener listener, String proposalId, TxId txId, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mTxId = txId;
        this.mProposalId = proposalId;
    }

    @Override
    Class<SignedTransaction> getClassOfValue() {
        return SignedTransaction.class;
    }

    @Override
    TxId getObject() {
        return mTxId;
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(TX_RELAY_STATUS.concat(mProposalId));
    }

    @Override
    protected void onSuccess(APIResult<SignedTransaction> result) {
        mListener.onSendRelayStatus(mProposalId);
    }

    protected void onFailure(APIResult<SignedTransaction> result) {
        if (result.getException() instanceof VolleyError && (208 == ((VolleyError) result.getException()).networkResponse.statusCode)) {
            mListener.onSendRelayStatus(mProposalId);
        } else {
            super.onFailure(result);
        }
    }

    public static void sendTxRelayStatus(ISendTxRelayStatusListener listener, String proposalId, TxId txId, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new SendTxRelayStatusOperation(listener, proposalId, txId, sessionId, nonce, secretKey, signer).buildRequest());
    }
}