package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IDeleteTxProposalListener;
import com.exawallet.api.results.APIResult;

import static com.android.volley.Request.Method.DELETE;
import static com.exawallet.common.AppContext.APP_CONTEXT;

public class DeleteTxProposalOperation extends AuthorizedStringEmptyResponseAPIOperation<IDeleteTxProposalListener> {
    private static final String TX_PROPOSALS = "tx_proposals/";
    private String mPropsalId;

    private DeleteTxProposalOperation(IDeleteTxProposalListener listener, String proposalId, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);

        mPropsalId = proposalId;
    }

    @Override
    int getMethod() {
        return DELETE;
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(TX_PROPOSALS).concat(mPropsalId);
    }

    @Override
    protected void onSuccess(APIResult<Object> result) {
        mListener.onDeleteTxProposal();
    }

    public static void deleteTxProposal(IDeleteTxProposalListener listener, String propsalId, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new DeleteTxProposalOperation(listener, propsalId, sessionId, nonce, secretKey, signer).buildRequest());
    }
}
