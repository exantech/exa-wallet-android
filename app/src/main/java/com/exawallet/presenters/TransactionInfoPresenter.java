package com.exawallet.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.exawallet.model.TransactionInfo;
import com.exawallet.monerowallet.R;
import com.exawallet.views.ITransactionInfoView;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.widget.Toast.makeText;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.model.NetworkType.NETWORK_TYPE_MAINNET;

public class TransactionInfoPresenter extends BaseSyncWalletPresenter<ITransactionInfoView> {
    private static final String MONERO_EXAN_TECH_PROD = "https://monero.exan.tech/search?value=";
    private static final String MONERO_EXAN_TECH_STAGE = "https://monero-stagenet.exan.tech/search?value=";
    private final TransactionInfo mTransactionInfo;

    public TransactionInfoPresenter(TransactionInfo transactionInfo) {
        this.mTransactionInfo = transactionInfo;
    }

    @Override
    public void onResume() {
        super.onResume();
        mView.showTransactionInfo(mTransactionInfo);
    }

    public void onCopyAddress() {
        ((ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("address", mTransactionInfo.getAddress()));
        makeText(mActivity, R.string.address_clipboard, Toast.LENGTH_SHORT).show();
    }

    public void onCopyHash() {
        ((ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("txhash", mTransactionInfo.hash));
        makeText(mActivity, R.string.txhash_clipboard, Toast.LENGTH_SHORT).show();
    }

    public void onCopyPaymentId() {
        ((ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("paymentId", mTransactionInfo.paymentId));
        makeText(mActivity, R.string.payment_id_clipboard, Toast.LENGTH_SHORT).show();
    }

    public void onGotoTxHash() {
        mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((NETWORK_TYPE_MAINNET == APP_CONTEXT.getNetworkType() ? MONERO_EXAN_TECH_PROD : MONERO_EXAN_TECH_STAGE) + mTransactionInfo.hash)));
    }
}