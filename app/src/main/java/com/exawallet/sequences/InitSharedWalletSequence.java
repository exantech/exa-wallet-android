package com.exawallet.sequences;

import com.exawallet.RootActivity;
import com.exawallet.api.results.InviteCode;
import com.exawallet.fragments.InviteCodeFragment;
import com.exawallet.utils.HLog;

import java.io.IOException;

import static com.exawallet.api.operations.CreateMultisigOperation.createMultisig;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.model.NativeCryptoUtils.cryptoUtils;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.model.meta.SharedStage.OBTAIN_MULTISIG_INFO;
import static com.exawallet.presenters.operations.QRCodeOperation.generateQRCode;

public class InitSharedWalletSequence extends CreateMultisigWalletSequence {
    private InitSharedWalletSequence(RootActivity activity, String address) {
        super(address, activity);
        TAG = InitSharedWalletSequence.class.getSimpleName();
    }

    @Override
    public void onTokenSend() {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> createMultisig(this, sharedMeta, walletMeta.getName(), APP_CONTEXT.getDeviceUid(), wallet.getMultisigInfo(), sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey)));
    }

    @Override
    public void onCreateMultisig(InviteCode result) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                creatingMeta.setInviteCode(result.getInviteCode());
                generateQRCode(mActivity, result.getInviteCode(), qrResult -> {
                    walletManager().setQRInviteCode(qrResult.getResult());
                    mActivity.show(InviteCodeFragment.newInstance());
                });

                nextStage(walletMeta, creatingMeta, OBTAIN_MULTISIG_INFO);
            } catch (IOException e) {
                HLog.error(TAG, "Can't updateInviteCode", e);
            }
        });
    }

    public static void initSharedWallet(RootActivity activity, String address) {
        onStage(new InitSharedWalletSequence(activity, address), address);
    }
}