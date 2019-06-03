package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.ITxProposalDecisionListener;
import com.exawallet.api.requests.TxProposalDecision;
import com.exawallet.api.results.APIResult;

import static com.android.volley.Request.Method.PUT;
import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SendTxProposalDecisionOperation extends AuthorizedJsonEmptyResponseAPIOperation<TxProposalDecision, ITxProposalDecisionListener> {
    private static final String DECISION = "/decision";
    private static final String TX_PROPOSALS = "tx_proposals/";
    private final TxProposalDecision mTxProposalDecision;
    private final String mPropsalId;

    private SendTxProposalDecisionOperation(ITxProposalDecisionListener listener, String propsalId, TxProposalDecision txProposalDecision, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mTxProposalDecision = txProposalDecision;
        this.mPropsalId = propsalId;
    }

    @Override
    TxProposalDecision getObject() {
        return mTxProposalDecision;
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(TX_PROPOSALS).concat(mPropsalId).concat(DECISION);
    }

    @Override
    protected void onSuccess(APIResult<Object> result) {
        mListener.onTxProposalDecision(mPropsalId);
    }

    @Override
    int getMethod() {
        return PUT;
    }

    public static void sendTxProposalDecision(ITxProposalDecisionListener listener, String propsalId, TxProposalDecision txProposalDecision, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new SendTxProposalDecisionOperation(listener, propsalId, txProposalDecision, sessionId, nonce, secretKey, signer).buildRequest());
    }
}