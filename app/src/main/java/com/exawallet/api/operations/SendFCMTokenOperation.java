package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IFCMTokenListener;
import com.exawallet.api.requests.FCMTokenInfo;
import com.exawallet.api.results.APIResult;
import com.exawallet.api.results.SubscribeResponse;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SendFCMTokenOperation extends AuthorizedJsonAPIOperation<SubscribeResponse, FCMTokenInfo, IFCMTokenListener> {
    private static final String PUSH_REGISTER = "push/register";
    private String mFcmToken;
    private final String mDeviceUid;

    private SendFCMTokenOperation(IFCMTokenListener listener, String fcmToken, String deviceUid, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
        this.mFcmToken = fcmToken;
        this.mDeviceUid = deviceUid;
    }

    @Override
    Class<SubscribeResponse> getClassOfValue() {
        return SubscribeResponse.class;
    }

    @Override
    FCMTokenInfo getObject() {
        return new FCMTokenInfo(mFcmToken, mDeviceUid);
    }

    @Override
    protected String getUrl() {
        return APP_CONTEXT.getApiURL().concat(PUSH_REGISTER);
    }

    @Override
    protected void onSuccess(APIResult<SubscribeResponse> result) {
        mListener.onTokenSend();
    }

    public static void sendFCMToken(IFCMTokenListener listener, String fcmToken, String deviceUid, String sessionId, int nonce, String secretKey, ISigner signer) {
        putRequest(new SendFCMTokenOperation(listener, fcmToken, deviceUid, sessionId, nonce, secretKey, signer).buildRequest());
    }
}