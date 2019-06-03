package com.exawallet.sequences;

import com.exawallet.RootActivity;
import com.exawallet.api.listeners.*;
import com.exawallet.api.requests.TxId;
import com.exawallet.api.requests.TxProposalDecision;
import com.exawallet.api.results.TxProposalsId;
import com.exawallet.common.ErrorMessage;
import com.exawallet.common.ErrorWithProcessMessage;
import com.exawallet.common.WalletRefreshed;
import com.exawallet.model.Wallet;
import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.monerowallet.R;
import com.exawallet.utils.HLog;

import java.io.IOException;

import static com.exawallet.api.operations.DeleteTxProposalOperation.deleteTxProposal;
import static com.exawallet.api.operations.ObtainLockOperation.obtainLockInfo;
import static com.exawallet.api.operations.SendTxProposalDecisionOperation.sendTxProposalDecision;
import static com.exawallet.api.operations.SendTxProposalsOperation.sendTxProposals;
import static com.exawallet.api.operations.SendTxRelayStatusOperation.sendTxRelayStatus;
import static com.exawallet.model.NativeCryptoUtils.cryptoUtils;
import static com.exawallet.model.meta.TxProposalStage.*;

public class ProcessTxProposalsSequence extends Sequence implements ITxProposalDecisionListener, ISendTxProposalsListener, ISendTxRelayStatusListener, IObtainLockListener, IDeleteTxProposalListener {
    private final String mAddress;
    private final TxProposalsMeta mTxProposalsMeta;

    private ProcessTxProposalsSequence(RootActivity activity, String address, TxProposalsMeta proposalsMeta) {
        super(activity);
        this.mAddress = address;
        this.mTxProposalsMeta = proposalsMeta;
    }

    private static void onStage(ProcessTxProposalsSequence sequence) {
        checkWallet(sequence.getAddress(), (wallet, walletInfo, sharedMeta, creatingMeta) -> {
            String sessionId = sharedMeta.getSessionId();
            Integer nonce = sharedMeta.getNonce();
            TxProposalsMeta txProposalsMeta = sequence.getTxProposalsMeta();

            switch (txProposalsMeta.getStage()) {
                case CREATE_TX_PROPOSALS: {
                    sendTxProposals(sequence, txProposalsMeta, sessionId, nonce, wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    break;
                }
                case SEND_TX_PROPOSALS_DECISION: {
                    try {
                        obtainLockInfo(sequence, txProposalsMeta.getId(), sessionId, nonce, wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        HLog.error(TAG, "Can't send txProposals relay status", e);
                    }
                    break;
                }
                case TX_PROPOSALS_RELAY: {
                    try {
                        sendTxRelayStatus(sequence, txProposalsMeta.getId(), new TxId(wallet.sendMultisigTransaction(txProposalsMeta, txProposalsMeta.getSignedTransaction())), sessionId, sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                    } catch (Exception e) {
                        sequence.post(new ErrorWithProcessMessage(e.getMessage(), R.string.delete_proposal, () -> deleteTxProposal(sequence, txProposalsMeta.getId(), sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey))));
                        HLog.error(TAG, "Can't send txProposals relay status", e);
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void onSendProposal(TxProposalsId result, TxProposalsMeta mTxProposalsMeta) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                mTxProposalsMeta.setStage(TX_PROPOSALS_DECISION_ACCEPTED);
                sharedMeta.replaceTxProposalsMeta(mTxProposalsMeta, result.getProposalId());

                walletMeta.store();
            } catch (IOException e) {
                HLog.error(TAG, "Can't sendProposal", e);
            }
        });
    }

    @Override
    public void onTxProposalDecision(String id) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                if (sharedMeta.getSigners() <= mTxProposalsMeta.getApprovalsCount()) {
                    mTxProposalsMeta.setStage(TX_PROPOSALS_RELAY);
                    sendTxRelayStatus(this, mTxProposalsMeta.getId(), new TxId(wallet.sendMultisigTransaction(mTxProposalsMeta, mTxProposalsMeta.getSignedTransaction())), sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
                } else {
                    mTxProposalsMeta.setStage(TX_PROPOSALS_DECISION_ACCEPTED);
                }

                walletMeta.store();
            } catch (Exception e) {
                post(new ErrorWithProcessMessage(e.getMessage(), R.string.delete_proposal, () -> deleteTxProposal(this, mTxProposalsMeta.getId(), sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey))));
                HLog.error(TAG, "Can't send txProposals relay status", e);
            }
        });
    }

    @Override
    public void onSendRelayStatus(String proposalId) {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                mTxProposalsMeta.setStage(TX_PROPOSALS_RELAY_DONE);
                walletMeta.store();
                wallet.store();

                HLog.debug(TAG, "Send exported multisig images " + sharedMeta.getOutput());
            } catch (Exception e) {
                HLog.error(TAG, "Can't send txProposals relay status", e);
            }
        });
    }

    @Override
    public void onLock() {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                boolean isApproved = mTxProposalsMeta.isApproved(wallet.getPublicMultisigSignerKey());

                HLog.debug(TAG, " getApprovalsCount " + mTxProposalsMeta.getApprovalsCount());

                sendTxProposalDecision(this, mTxProposalsMeta.getId(), new TxProposalDecision(isApproved ? signTransaction(wallet, mTxProposalsMeta) : "", isApproved, mTxProposalsMeta.getApprovalsCount() - 1), sharedMeta.getSessionId(), sharedMeta.getNonce(), wallet.getSecretSpendKey(), (message, secretKey) -> cryptoUtils().signMessageJ(message, secretKey));
            } catch (Exception e) {
                post(new ErrorMessage(e.getMessage()));
                HLog.error(TAG, "Can't sign transaction  ", e);

                mTxProposalsMeta.reject(wallet.getPublicMultisigSignerKey());
            }
        });
    }

    @Override
    public void onDeleteTxProposal() {
        checkWallet(mAddress, (wallet, walletMeta, sharedMeta, creatingMeta) -> {
            try {
                mTxProposalsMeta.setStage(TX_PROPOSALS_DECISION_ACCEPTED);
                walletMeta.store();
                wallet.store();

                HLog.debug(TAG, "Send exported multisig images " + sharedMeta.getOutput());
            } catch (Exception e) {
                HLog.error(TAG, "Can't send txProposals relay status", e);
            }
        });
    }

    private TxProposalsMeta getTxProposalsMeta() {
        return mTxProposalsMeta;
    }

    private String getAddress() {
        return mAddress;
    }

    static void processProposal(RootActivity activity, String address, TxProposalsMeta proposalsMeta) {
        onStage(new ProcessTxProposalsSequence(activity, address, proposalsMeta));
    }

    private static String signTransaction(Wallet wallet, TxProposalsMeta txProposalsMeta) throws Exception {
        return wallet.restoreMultisigTransaction(txProposalsMeta.getSignedTransaction());
    }
}