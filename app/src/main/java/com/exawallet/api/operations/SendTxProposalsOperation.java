package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.ISendTxProposalsListener;
import com.exawallet.api.requests.TxProposalsSigned;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.TxProposalsId;
import com.exawallet.model.meta.TxProposalsMeta;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SendTxProposalsOperation extends AuthorizedJsonAPIOperation<TxProposalsId, TxProposalsSigned, ISendTxProposalsListener> {
    private static final String TX_PROPOSALS = "tx_proposals";
    private final TxProposalsMeta mTxProposalsMeta;

    private SendTxProposalsOperation(ISendTxProposalsListener listener, TxProposalsMeta txProposalsMeta, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        mTxProposalsMeta = txProposalsMeta;
    }

    @Override
    Class<TxProposalsId> getClassOfValue() {
        return TxProposalsId.class;
    }

    @Override
    TxProposalsSigned getObject() {
        return mTxProposalsMeta.getTxProposals();
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(TX_PROPOSALS);
    }

    @Override
    protected void onSuccess(APIResult<TxProposalsId> result) {
        mListener.onSendProposal(result.getResult(), mTxProposalsMeta);
    }

    public static void sendTxProposals(ISendTxProposalsListener listener, TxProposalsMeta txProposalsMeta, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new SendTxProposalsOperation(listener, txProposalsMeta, sessionId, nonce, secretKey, signer).buildRequest());
    }
}