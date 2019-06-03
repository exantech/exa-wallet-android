package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IJoinMultisigListener;
import com.exawallet.api.requests.JoinInfo;
import com.exawallet.api.results.APIResult;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class JoinSharedOperation extends AuthorizedJsonEmptyResponseAPIOperation<JoinInfo, IJoinMultisigListener> {
    private static final String JOIN_WALLET = "join_wallet";
    private final String mInviteCode;
    private String mDeviceUid;
    private final String mMultisigInfo;

    private JoinSharedOperation(IJoinMultisigListener listener, String inviteCode, String deviceUid, String multisigInfo, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mInviteCode = inviteCode;
        mDeviceUid = deviceUid;
        this.mMultisigInfo = multisigInfo;
    }

    @Override
    JoinInfo getObject() {
        return new JoinInfo(mInviteCode, mDeviceUid, mMultisigInfo);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(JOIN_WALLET);
    }

    @Override
    protected void onSuccess(APIResult<Object> result) {
        mListener.onJoinMultisig();
    }

    public static void joinShared(IJoinMultisigListener listener, String inviteCode, String deviceUid, String multisigInfo, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new JoinSharedOperation(listener, inviteCode, deviceUid, multisigInfo, sessionId, nonce, secretKey, signer).buildRequest());
    }
}