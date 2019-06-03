package com.exawallet.sequences;

import com.android.volley.VolleyError;
import com.exawallet.RootActivity;
import com.exawallet.api.listeners.*;
import com.exawallet.api.results.*;
import com.exawallet.common.ErrorMessage;
import com.exawallet.model.meta.CreatingMeta;
import com.exawallet.model.meta.SharedStage;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.monerowallet.R;
import com.exawallet.utils.HLog;

import java.io.IOException;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.api.operations.CreateMultisigOperation.createMultisig;
import static com.exawallet.api.operations.JoinSharedOperation.joinShared;
import static com.exawallet.api.operations.ObtainExtraMultisigInfoOperation.obtainExtraMultisigInfo;
import static com.exawallet.api.operations.ObtainMultisigInfoOperation.obtainMultisigInfo;
import static com.exawallet.api.operations.ObtainWalletInfoOperation.obtainWalletInfo;
import static com.exawallet.api.operations.OpenSessionOperation.openSession;
import static com.exawallet.api.operations.SendExtraMultisigOperation.sendExtraMultisigInfo;
import static com.exawallet.api.operations.SendFCMTokenOperation.sendFCMToken;
import static com.exawallet.api.operations.SendPublicKeyOperation.sendPublicKey;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.common.AppContext.setCurrentWalletAddress;
import static com.exawallet.model.NativeCryptoUtils.cryptoUtils;
import static com.exawallet.model.meta.SharedStage.*;
import static com.exawallet.presenters.operations.DelayOperation.schedule;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.WALLETS;
import static com.exawallet.utils.Utils.selectWalletScreen;

abstract class CreateMultisigWalletSequence extends Sequence implements IOpenSessionListener, IFCMTokenListener, IJoinMultisigListener, ICreateMultisigListener, ISendPublicKeyListener, ISendExtraMultisigListener, IObtainMultisigInfoListener, IObtainExtraMultisigInfoListener, IObtainWalletInfoListener {
    static String TAG = InitSharedWalletSequence.class.getSimpleName();
    final String mAddress;

    CreateMultisigWalletSequence(String address, RootActivity activity) {
        super(activity);
        this.mAddress = address;
    }

    static void onStage(CreateMultisigWalletSequence sequence, String address) {
        checkWallet(address, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            String sessionId = sharedMeta.getSessionId();

            switch (creatingMeta.getStage()) {
                case OPEN_SESSION: {
                    openSession(sequence, wallet.getPublicSpendKey());
                    break;
                }
                case REGISTER_PUSH_TOKEN: {
                    try {
                        sendFCMToken(sequence, APP_CONTEXT.getFCMToken(), APP_CONTEXT.getDeviceUid(), sessionId, sharedMeta.getNonce(), sharedMeta.getSecretSpendKey(APP_CONTEXT.getDeviceUid()), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorMessage(e.getMessage()));
                        HLog.error(TAG, "Can't send FCM token", e);
                    }
                    break;
                }
                case OBTAIN_WALLET_INFO: {
                    try {
                        obtainWalletInfo(sequence, sessionId, sharedMeta.getNonce(), sharedMeta.getSecretSpendKey(APP_CONTEXT.getDeviceUid()), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorMessage(e.getMessage()));
                        HLog.error(TAG, "Can't obtain WalletInfo", e);
                    }
                    break;
                }
                case CREATE_MULTIGIG: {
                    try {
                        createMultisig(sequence, sharedMeta, walletMeta.getName(), APP_CONTEXT.getDeviceUid(), wallet.getMultisigInfo(), sessionId, sharedMeta.getNonce(), sharedMeta.getSecretSpendKey(APP_CONTEXT.getDeviceUid()), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorMessage(e.getMessage()));
                        HLog.error(TAG, "Can't create multisig wallet", e);
                    }
                    break;
                }
                case JOIN_MULTIGIG: {
                    try {
                        joinShared(sequence, creatingMeta.getInviteCode(), APP_CONTEXT.getDeviceUid(), wallet.getMultisigInfo(), sessionId, sharedMeta.getNonce(), sharedMeta.getSecretSpendKey(APP_CONTEXT.getDeviceUid()), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorMessage(e.getMessage()));
                        HLog.error(TAG, "Can't join multisig wallet", e);
                    }
                    break;
                }
                case OBTAIN_MULTISIG_INFO: {
                    try {
                        obtainMultisigInfo(sequence, sessionId, sharedMeta.getNonce(), sharedMeta.getSecretSpendKey(APP_CONTEXT.getDeviceUid()), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorMessage(e.getMessage()));
                        HLog.error(TAG, "Can't obtain multisigInfo", e);
                    }
                    break;
                }
                case CHANGE_PUBLIS_KEY: {
                    try {
                        sendPublicKey(sequence, wallet.getPublicMultisigSignerKey(), sessionId, sharedMeta.getNonce(), sharedMeta.getSecretSpendKey(APP_CONTEXT.getDeviceUid()), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorMessage(e.getMessage()));
                        HLog.error(TAG, "Can't send publicKey", e);
                    }
                    break;
                }
                case SEND_EXTRA_MULTISIG_INFO: {
                    try {
                        sendExtraMultisigInfo(sequence, creatingMeta.getExtraMultisigInfo(), creatingMeta.getCounter(), sessionId, sharedMeta.getNonce(), sharedMeta.getSecretSpendKey(APP_CONTEXT.getDeviceUid()), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorMessage(e.getMessage()));
                        HLog.error(TAG, "Can't send extra multisigInfo", e);
                    }
                    break;
                }
                case OBTAIN_EXTRA_MULTISIG_INFO: {
                    try {
                        obtainExtraMultisigInfo(sequence, creatingMeta.getCounter(), sessionId, sharedMeta.getNonce(), sharedMeta.getSecretSpendKey(APP_CONTEXT.getDeviceUid()), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorMessage(e.getMessage()));
                        HLog.error(TAG, "Can't obtain extra multisigInfo", e);
                    }
                    break;
                }
            }
        });
    }

    void nextStage(WalletMeta walletMeta, CreatingMeta creatingMeta, SharedStage stage) throws IOException {
        creatingMeta.setStage(stage);
        walletMeta.store();
        onStage(this, mAddress);
    }

    @Override
    public void onCreateMultisig(InviteCode result) {
    }

    @Override
    public void onJoinMultisig() {
    }

    @Override
    public void onObtainWalletInfo(WalletInfo walletInfo) {
    }

    public void onOpenSession(SessionId result) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                sharedMeta.setSecretSpendKey(wallet.getSecretSpendKey(), APP_CONTEXT.getDeviceUid());
                sharedMeta.setSeed(wallet.getSeed(), APP_CONTEXT.getDeviceUid());
                sharedMeta.setSessionId(result.getSessionId());
                nextStage(walletMeta, creatingMeta, REGISTER_PUSH_TOKEN);
            } catch (Exception e) {
                HLog.error(TAG, "Can't updateInviteCode", e);
            }
        });
    }

    @Override
    public void onObtainMultisigInfos(MultisigInfos result) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                creatingMeta.setMultisigInfos(result.getMultisigInfos());

                if (null != result.getMultisigInfos() && sharedMeta.getParticipants() == result.getMultisigInfos().length) {
                    creatingMeta.setMultisigInfos(null);
                    creatingMeta.setExtraMultisig(wallet.makeMultisigJ(result.getMultisigInfos(), sharedMeta.getSigners()), sharedMeta.isN1Scheme());

                    nextStage(walletMeta, creatingMeta, isEmpty(creatingMeta.getExtraMultisigInfo()) ? CHANGE_PUBLIS_KEY : SEND_EXTRA_MULTISIG_INFO);
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
    public final void onSendExtraMultisig() {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                nextStage(walletMeta, creatingMeta, OBTAIN_EXTRA_MULTISIG_INFO);
            } catch (IOException e) {
                HLog.error(TAG, "Can't exchangeExtraMultisig", e);
            }
        });
    }

    @Override
    public void onObtainExtraMultisigKeys(ExtraMultisigKeys result) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                if (null != result.getKeys() && sharedMeta.getParticipants() <= result.getKeys().length) {
                    creatingMeta.setExtraMultisig(wallet.exchangeMultisigKeysJ(result.getKeys()), sharedMeta.isN1Scheme());

                    nextStage(walletMeta, creatingMeta, isEmpty(creatingMeta.getExtraMultisigInfo()) ? CHANGE_PUBLIS_KEY : SEND_EXTRA_MULTISIG_INFO);
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
    public final void onSendPublicKey() {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                sharedMeta.setSecretSpendKey(wallet.getSecretSpendKey(), wallet.getSecretSpendKey());
                sharedMeta.setSeed(sharedMeta.getSeed(APP_CONTEXT.getDeviceUid()), wallet.getSecretSpendKey());
                sharedMeta.setCreatingMeta(null);

                walletMeta.updateAddress(wallet.getAddress());

                wallet.store();

                showWalletScreen(walletMeta);
            } catch (Exception e) {
                HLog.error(TAG, "Can't sendPublicKey", e);
            }

            post(DONE);
        });
    }

    @Override
    public void onFailure(Exception exception) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            if (exception instanceof VolleyError && null != ((VolleyError) exception).networkResponse) {
                if (400 == ((VolleyError) exception).networkResponse.statusCode && null != creatingMeta && SEND_EXTRA_MULTISIG_INFO == creatingMeta.getStage()) {
                    try {
                        nextStage(walletMeta, creatingMeta, OBTAIN_EXTRA_MULTISIG_INFO);
                    } catch (IOException e) {
                        HLog.error(TAG, "Can't process exception", e);
                    }
                } else if (410 == ((VolleyError) exception).networkResponse.statusCode) {
                    sharedMeta.setSessionId(null);
                    openSession(new IOpenSessionListener() {
                        @Override
                        public void onOpenSession(SessionId result) {
                            sharedMeta.setSessionId(result.getSessionId());
                            onStage(CreateMultisigWalletSequence.this, mAddress);
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            CreateMultisigWalletSequence.this.onFailure(exception);
                        }
                    }, wallet.getPublicSpendKey());
                } else if (409 == ((VolleyError) exception).networkResponse.statusCode) {
                    BACK_PATH.clear();
                    mActivity.goScreen(mActivity.getString(R.string.wallets_one_device), WALLETS);
                } else if (500 == ((VolleyError) exception).networkResponse.statusCode) {
                    BACK_PATH.clear();
                    mActivity.goScreen(mActivity.getString(R.string.internal_server_error), WALLETS);
                } else {
                    schedule(() -> onStage(this, mAddress), 15);
                }
            } else {
                schedule(() -> onStage(this, mAddress), 15);
            }
        });
    }

    void showWalletScreen(WalletMeta walletMeta) {
        try {
            walletMeta.store();

            BACK_PATH.clear();
            BACK_PATH.setScreen(WALLETS);
            setCurrentWalletAddress(walletMeta.getAddress());
            selectWalletScreen(walletMeta, mActivity);
        } catch (Exception e) {
            HLog.error(TAG, "Can't sendPublicKey", e);
        }
    }
}