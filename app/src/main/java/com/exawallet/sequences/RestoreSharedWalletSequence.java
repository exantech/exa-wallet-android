package com.exawallet.sequences;

import com.android.volley.VolleyError;
import com.exawallet.RootActivity;
import com.exawallet.api.results.ExtraMultisigKeys;
import com.exawallet.api.results.MultisigInfos;
import com.exawallet.api.results.WalletInfo;
import com.exawallet.utils.HLog;

import java.io.IOException;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.meta.SharedStage.*;
import static com.exawallet.presenters.operations.DelayOperation.schedule;

public class RestoreSharedWalletSequence extends CreateMultisigWalletSequence {
    private RestoreSharedWalletSequence(String address, RootActivity activity) {
        super(address, activity);
    }

    @Override
    public void onTokenSend() {
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

                nextStage(walletMeta, creatingMeta, OBTAIN_MULTISIG_INFO);
            } catch (IOException e) {
                HLog.error(TAG, "Can't updateJoined", e);
            }
        });
    }

    @Override
    public final void onObtainMultisigInfos(MultisigInfos result) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                creatingMeta.setMultisigInfos(result.getMultisigInfos());

                if (null != result.getMultisigInfos() && sharedMeta.getParticipants() == result.getMultisigInfos().length) {
                    creatingMeta.setMultisigInfos(null);
                    creatingMeta.setExtraMultisig(wallet.makeMultisigJ(result.getMultisigInfos(), sharedMeta.getSigners()), sharedMeta.isN1Scheme());

                    nextStage(walletMeta, creatingMeta, isEmpty(creatingMeta.getExtraMultisigInfo()) ? CHANGE_PUBLIS_KEY : OBTAIN_EXTRA_MULTISIG_INFO);
                } else {
                    schedule(() -> onStage(this, mAddress), 15);
                }

                walletMeta.store();
            } catch (IOException e) {
                HLog.error(TAG, "Can't make multisigInfo", e);
            }
        });
    }

    @Override
    public final void onObtainExtraMultisigKeys(ExtraMultisigKeys result) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                if (null != result.getKeys() && sharedMeta.getParticipants() <= result.getKeys().length) {
                    creatingMeta.setExtraMultisig(wallet.exchangeMultisigKeysJ(result.getKeys()), sharedMeta.isN1Scheme());

                    nextStage(walletMeta, creatingMeta, isEmpty(creatingMeta.getExtraMultisigInfo()) ? CHANGE_PUBLIS_KEY : OBTAIN_EXTRA_MULTISIG_INFO);
                } else {
                    schedule(() -> onStage(this, mAddress), 15);
                }

                walletMeta.store();
            } catch (IOException e) {
                HLog.error(TAG, "Can't finalizeMultisig", e);
            }
        });
    }

    @Override
    public void onFailure(Exception exception) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            if (exception instanceof VolleyError && 400 == ((VolleyError) exception).networkResponse.statusCode && null != creatingMeta && OBTAIN_WALLET_INFO == creatingMeta.getStage()) {
                walletMeta.setSharedMeta(null);

                showWalletScreen(walletMeta);
            } else {
                super.onFailure(exception);
            }
        });
    }

    public static void restoreSharedWallet(RootActivity activity, String address) {
        onStage(new RestoreSharedWalletSequence(address, activity), address);
    }
}