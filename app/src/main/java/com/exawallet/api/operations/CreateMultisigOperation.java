package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.ICreateMultisigListener;
import com.exawallet.api.requests.MultisigInfo;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.InviteCode;
import com.exawallet.model.meta.SharedMeta;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class CreateMultisigOperation extends AuthorizedJsonAPIOperation<InviteCode, MultisigInfo, ICreateMultisigListener> {
    private static final String CREATE_WALLET = "create_wallet";
    private final SharedMeta mSharedMeta;
    private final String mName;
    private String mDeviceUid;
    private final String mMultisigInfo;

    private CreateMultisigOperation(ICreateMultisigListener listener, SharedMeta sharedMeta, String name, String deviceUid, String multisigInfo, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mSharedMeta = sharedMeta;
        this.mName = name;
        this.mDeviceUid = deviceUid;
        this.mMultisigInfo = multisigInfo;
    }

    @Override
    Class<InviteCode> getClassOfValue() {
        return InviteCode.class;
    }

    @Override
    MultisigInfo getObject() {
        return new MultisigInfo(mSharedMeta.getParticipants(), mSharedMeta.getSigners(), mName, mDeviceUid, mMultisigInfo);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(CREATE_WALLET);
    }

    @Override
    protected void onSuccess(APIResult<InviteCode> result) {
        mListener.onCreateMultisig(result.getResult());
    }

    public static void createMultisig(ICreateMultisigListener listener, SharedMeta sharedMeta, String name, String deviceUid, String multisigInfo, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new CreateMultisigOperation(listener, sharedMeta, name, deviceUid, multisigInfo, sessionId, nonce, secretKey, signer).buildRequest());
    }
}