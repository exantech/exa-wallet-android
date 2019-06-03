package com.exawallet.sequences;

import com.android.volley.VolleyError;
import com.exawallet.RootActivity;
import com.exawallet.api.listeners.IObtainOutputsListener;
import com.exawallet.api.listeners.IObtainTxProposalsInfoListener;
import com.exawallet.api.listeners.IOpenSessionListener;
import com.exawallet.api.listeners.ISendOutputsListener;
import com.exawallet.api.requests.TxProposalsInfo;
import com.exawallet.api.results.Outputs;
import com.exawallet.api.results.SessionId;
import com.exawallet.common.WalletRefreshed;
import com.exawallet.model.TransactionInfo;
import com.exawallet.model.meta.TxProposalStage;
import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.utils.HLog;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.api.operations.ObtainOutputsOperation.obtainOutputs;
import static com.exawallet.api.operations.ObtainTxProposalsInfoOperation.obtainTxProposalsInfo;
import static com.exawallet.api.operations.OpenSessionOperation.openSession;
import static com.exawallet.api.operations.SendOutputsOperation.sendOutputs;
import static com.exawallet.api.requests.TxProposalsStatus.*;
import static com.exawallet.model.NativeCryptoUtils.cryptoUtils;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.model.meta.TxProposalStage.*;
import static com.exawallet.sequences.ProcessTxProposalsSequence.processProposal;
import static com.exawallet.utils.Utils.containString;

public class SyncWalletSequence extends Sequence implements ISendOutputsListener, IObtainOutputsListener, IObtainTxProposalsInfoListener, IOpenSessionListener {
    protected static final String TAG = SyncWalletSequence.class.getSimpleName();
    private static Sequence sInstance;
    protected String mAddress;

    private SyncWalletSequence(RootActivity activity) {
        super(activity);
        mEventBus.register(this);
    }

    @Subscribe
    public void onEvent(final WalletRefreshed event) {
        checkWallet(mAddress = walletManager().getWallet().getAddress(), (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            if (walletMeta.isSharedReady()) {
                String sessionId = sharedMeta.getSessionId();
                if (isEmpty(sessionId)) {
                    openSession(this, wallet.getPublicSpendKey());
                } else {
                    obtainOutputs(this, sessionId, sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                }
            }
        });
    }

    @Override
    public void onSendOutputs() {
        checkWallet(mAddress, ((wallet, walletMeta, sharedMeta, creatingMeta) -> obtainOutputs(this, sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey))));
    }

    @Override
    public void onObtainOutputs(Outputs result) {
        String[] outputs = result.getOutputs();
        if (null != outputs && 0 < outputs.length) {
            checkWallet(mAddress, ((wallet, walletMeta, sharedMeta, creatingMeta) -> {
                sharedMeta.setObtained(outputs);

                obtainTxProposalsInfo(this, sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
            }));
        }
    }

    @Override
    public void onObtainTxProposalsInfo(final TxProposalsInfo[] proposalsInfos) {
        checkWallet(mAddress, ((wallet, walletMeta, sharedMeta, creatingMeta) -> {
            boolean changeProposalStatus = false;

            if (null != proposalsInfos && 0 < proposalsInfos.length) {
                String publicMultisigSignerKey = wallet.getPublicMultisigSignerKey();
                for (TxProposalsInfo proposalsInfo : proposalsInfos) {
                    String proposalId = proposalsInfo.getProposalId();

                    if (null == sharedMeta.getTxProposalsMeta(proposalId)) {
                        TxProposalsMeta txProposalsMeta = new TxProposalsMeta(proposalsInfo.getLastSignedTransaction(), proposalId, proposalsInfo.getDestinationAddress(), proposalsInfo.getDescription(), proposalsInfo.getAmount(), proposalsInfo.getFee(), proposalsInfo.getApprovals(), proposalsInfo.getRejects(), getStage(sharedMeta.getParticipants(), sharedMeta.getSigners(), proposalsInfo.getApprovals(), proposalsInfo.getRejects(), publicMultisigSignerKey));

                        changeProposalStatus |= relayed == proposalsInfo.getStatus() || rejected == proposalsInfo.getStatus() || deleted == proposalsInfo.getStatus();
                        if (changeProposalStatus) {
                            txProposalsMeta.setStage(OUTPUT_EXPORTED);
                        }

                        sharedMeta.setTxProposalRequest(txProposalsMeta);
                    } else {
                        TxProposalsMeta txProposalsMeta = sharedMeta.getTxProposalsMeta(proposalId);
                        changeProposalStatus |= OUTPUT_EXPORTED != txProposalsMeta.getStage() && (relayed == proposalsInfo.getStatus() || rejected == proposalsInfo.getStatus() || deleted == proposalsInfo.getStatus());

                        if (changeProposalStatus) {
                            txProposalsMeta.setStage(OUTPUT_EXPORTED);
                        } else {
                            txProposalsMeta.setStage(!proposalsInfo.hasDecision(publicMultisigSignerKey) && txProposalsMeta.hasDecision(publicMultisigSignerKey) ? SEND_TX_PROPOSALS_DECISION : txProposalsMeta.getStage());
                        }

                        txProposalsMeta.setSignedTransaction(proposalsInfo.getLastSignedTransaction());
                        txProposalsMeta.updateApprovals(proposalsInfo.getApprovals(), publicMultisigSignerKey);
                        txProposalsMeta.updateRejects(proposalsInfo.getRejects(), publicMultisigSignerKey);
                    }
                }
            }

            TxProposalsMeta proposal = selectProposal(sharedMeta.getTxProposalMetas());
            if (null != proposal) {
                if (changeProposalStatus) {
                    proposal.reject(wallet.getPublicMultisigSignerKey());
                }
                processProposal(mActivity, mAddress, proposal);
            }

            boolean changeTransactionCount = walletMeta.getTransactionCount() < wallet.getHistory().getCount();
            boolean isConfirmed = true;

            for (TransactionInfo transactionInfo : wallet.getHistory().getAll(wallet)) {
                isConfirmed &= 0 < transactionInfo.confirmations;
            }

            if ((changeTransactionCount && isConfirmed && null == proposal)
                    || (!changeTransactionCount && (changeProposalStatus || sharedMeta.isImportExpired()))) {
                try {
                    walletMeta.setTransactionCount(wallet.getHistory().getCount());
                    sharedMeta.setOutput(wallet.exportMultisigImages());

                    if (sharedMeta.needInitImported()) {
                        sharedMeta.setImported(sharedMeta.getObtained());
                    }

                    walletMeta.store();
                    wallet.store();
                } catch (Exception e) {
                    HLog.error(TAG, "Can't send txProposals relay status", e);
                }
            }

            if (sharedMeta.needSendOutput()) {
                sendOutputs(this, sharedMeta.getOutput(), sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
            } else if (sharedMeta.needImport()) {
                try {
                    wallet.importMultisigImages(sharedMeta.getObtained());
                    sharedMeta.setImported(sharedMeta.getObtained());
                    walletMeta.store();
                    wallet.store();
                } catch (GeneralSecurityException | IOException e) {
                    HLog.error(TAG, "Can't update outputImageImport " + e.getMessage(), e);
                }
            }
        }));
    }

    private TxProposalsMeta selectProposal(List<TxProposalsMeta> txProposalMetas) {
        if (null != txProposalMetas) {
            for (TxProposalsMeta proposalsMeta : txProposalMetas) {
                if (proposalsMeta.isExistsInProcess()) {
                    return proposalsMeta;
                }
            }

            for (TxProposalsMeta proposalsMeta : txProposalMetas) {
                if (CREATE_TX_PROPOSALS == proposalsMeta.getStage()) {
                    return proposalsMeta;
                }
            }
        }

        return null;
    }

    private boolean isDecisionAccepted(int participants, int signers, String[] approvals, String[] rejects) {
        return (null != approvals && signers <= approvals.length) || (null != rejects && participants - signers < rejects.length);
    }

    private TxProposalStage getStage(int participants, int signers, String[] approvals, String[] rejects, String publicMultisigSignerKey) {
        return containString(approvals, publicMultisigSignerKey) || containString(rejects, publicMultisigSignerKey) || isDecisionAccepted(participants, signers, approvals, rejects)
                ? TX_PROPOSALS_DECISION_ACCEPTED : EXPECT_TX_PROPOSALS_DECISION;
    }

    @Override
    public void onOpenSession(SessionId result) {
        checkWallet(mAddress, ((wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                sharedMeta.setSessionId(result.getSessionId());
                walletMeta.store();
                obtainOutputs(this, sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
            } catch (IOException e) {
                HLog.error(TAG, "Can't update sessionId", e);
            }
        }));
    }

    @Override
    public void onFailure(Exception exception) {
        checkWallet(mAddress, ((wallet, walletMeta, sharedMeta, creatingMeta) -> {
            if (exception instanceof VolleyError && null != ((VolleyError) exception).networkResponse && (403 == ((VolleyError) exception).networkResponse.statusCode)) {
                sharedMeta.setSessionId(null);
            } else {
                super.onFailure(exception);
            }
        }));
    }

    public static void startWalletSync(RootActivity activity) {
        sInstance = new SyncWalletSequence(activity);
    }
}