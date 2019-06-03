package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.presenters.results.SignedTransactionResult;

import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.PendingTransaction.Priority.Priority_Default;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.model.meta.TxProposalStage.CREATE_TX_PROPOSALS;
import static java.lang.System.currentTimeMillis;

public class CreateTxProposalOperation extends SplashScreenOperation<SignedTransactionResult> {
    private static final int MIXIN = 7;
    private final String mAddress;
    private final String mPaymentId;
    private final String mPaymentNote;
    private final long mAmount;
    private final String mKey;

    private CreateTxProposalOperation(String dstAddr, String paymentId, String paymentNote, long amount, String publicMultisigSignerKey, RootActivity activity) {
        super(activity);
        this.mAddress = dstAddr;
        this.mPaymentId = paymentId;
        this.mPaymentNote = paymentNote;
        this.mAmount = amount;
        this.mKey = publicMultisigSignerKey;
    }

    @Override
    protected void onSuccess(SignedTransactionResult result) {
        walletManager().getWallet().getWalletMeta().getSharedMeta().setTxProposalRequest(new TxProposalsMeta(result.getResult(), String.valueOf(currentTimeMillis()), mAddress, mPaymentNote, mAmount, -1L, new String[]{mKey}, null, CREATE_TX_PROPOSALS));
        mActivity.goBack();
    }

    @Override
    protected SignedTransactionResult execute() {
        try {
            return new SignedTransactionResult(walletManager().getWallet().createMultisigTransaction(mAddress, mPaymentId, mPaymentNote, mAmount, MIXIN, Priority_Default));
        } catch (Exception e) {
            return new SignedTransactionResult(e);
        }
    }

    public static void createTxProposals(String dstAddr, String paymentId, String paymentNote, long amount, String publicMultisigSignerKey, RootActivity activity) {
        ENGINE.submit(new CreateTxProposalOperation(dstAddr, paymentId, paymentNote, amount, publicMultisigSignerKey, activity));
    }
}