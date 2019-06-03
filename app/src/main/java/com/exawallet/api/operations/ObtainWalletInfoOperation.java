package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IObtainWalletInfoListener;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.WalletInfo;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class ObtainWalletInfoOperation extends AuthorizedStringAPIOperation<WalletInfo, IObtainWalletInfoListener> {
    private static final String INFO_WALLET = "info/wallet";

    private ObtainWalletInfoOperation(IObtainWalletInfoListener listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(INFO_WALLET);
    }

    @Override
    Class<WalletInfo> getClassOfValue() {
        return WalletInfo.class;
    }

    @Override
    protected void onSuccess(APIResult<WalletInfo> result) {
        mListener.onObtainWalletInfo(result.getResult());
    }

    @Override
    protected void onFailure(APIResult<WalletInfo> result) {
        super.onFailure(result);
    }

    public static void obtainWalletInfo(IObtainWalletInfoListener listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new ObtainWalletInfoOperation(listener, sessionId, nonce, secretKey, signer).buildRequest());
    }
}