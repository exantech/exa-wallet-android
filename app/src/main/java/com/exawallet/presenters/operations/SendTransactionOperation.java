package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.presenters.results.TransactionResult;
import com.exawallet.utils.HLog;

import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.PendingTransaction.Priority.Priority_Default;
import static com.exawallet.model.WalletManager.walletManager;

public class SendTransactionOperation extends SplashScreenOperation<TransactionResult> {
    private static final int MIXIN = 7;
    private final String mAddress;
    private final String mPaymentId;
    private final String mPaymentNote;
    private final long mAmount;

    private SendTransactionOperation(String dstAddr, String paymentId, String paymentNote, long amount, RootActivity activity) {
        super(activity);
        this.mAddress = dstAddr;
        this.mPaymentId = paymentId;
        this.mPaymentNote = paymentNote;
        this.mAmount = amount;
    }

    @Override
    protected TransactionResult execute() {
        try {
            return new TransactionResult(walletManager().getWallet().sendTransaction(mAddress, mPaymentId, mPaymentNote, mAmount, MIXIN, Priority_Default));
        } catch (Exception e) {
            return new TransactionResult(e);
        }
    }

    @Override
    protected void onSuccess(TransactionResult result) {
        HLog.debug(TAG, "onSuccess");
        mActivity.goBack();
    }

    public static void sendTransaction(String dstAddr, String paymentId, String paymentNote, long amount, RootActivity activity) {
        ENGINE.submit(new SendTransactionOperation(dstAddr, paymentId, paymentNote, amount, activity));
    }
}