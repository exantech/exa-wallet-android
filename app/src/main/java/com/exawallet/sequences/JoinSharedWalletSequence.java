package com.exawallet.sequences;

import com.android.volley.VolleyError;
import com.exawallet.RootActivity;
import com.exawallet.api.results.WalletInfo;
import com.exawallet.common.ErrorMessage;
import com.exawallet.fragments.InviteCodeFragment;
import com.exawallet.monerowallet.R;
import com.exawallet.utils.HLog;

import java.io.IOException;

import static com.exawallet.api.operations.JoinSharedOperation.joinShared;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.model.NativeCryptoUtils.cryptoUtils;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.model.meta.SharedStage.*;
import static com.exawallet.presenters.operations.QRCodeOperation.generateQRCode;

public class JoinSharedWalletSequence extends CreateMultisigWalletSequence {
    private JoinSharedWalletSequence(RootActivity activity, String address) {
        super(address, activity);
        TAG = InitSharedWalletSequence.class.getSimpleName();
    }

    @Override
    public void onTokenSend() {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> joinShared(this, creatingMeta.getInviteCode(), APP_CONTEXT.getDeviceUid(), wallet.getMultisigInfo(), sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey)));
    }

    @Override
    public void onJoinMultisig() {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                nextStage(walletMeta, creatingMeta, OBTAIN_WALLET_INFO);
            } catch (IOException e) {
                HLog.error(TAG, "Can't updateJoined", e);
            }
        });
    }

    @Override
    public void onObtainWalletInfo(WalletInfo walletInfo) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                sharedMeta.setParticipants(walletInfo.getParticipants());
                sharedMeta.setSigners(walletInfo.getSigners());

                generateQRCode(mActivity, creatingMeta.getInviteCode(), qrResult -> {
                    walletManager().setQRInviteCode(qrResult.getResult());
                    mActivity.show(InviteCodeFragment.newInstance());
                });

                nextStage(walletMeta, creatingMeta, OBTAIN_MULTISIG_INFO);
            } catch (IOException e) {
                HLog.error(TAG, "Can't updateJoined", e);
            }
        });
    }

    @Override
    public void onFailure(Exception exception) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            if (exception instanceof VolleyError && 409 == ((VolleyError) exception).networkResponse.statusCode && null != creatingMeta && JOIN_MULTIGIG == creatingMeta.getStage()) {
                post(new ErrorMessage(mActivity.getString(R.string.wallet_fulfilled)));
            } else {
                super.onFailure(exception);
            }
        });
    }

    public static void joinSharedWallet(RootActivity activity, String address) {
        onStage(new JoinSharedWalletSequence(activity, address), address);
    }
}