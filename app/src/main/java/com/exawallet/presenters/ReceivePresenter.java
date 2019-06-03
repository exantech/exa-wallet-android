package com.exawallet.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;
import com.exawallet.common.QRAddress;
import com.exawallet.monerowallet.R;
import com.exawallet.utils.SimpleTextWatcher;
import com.exawallet.views.IReceiveView;
import org.greenrobot.eventbus.Subscribe;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.makeText;
import static com.exawallet.model.Wallet.generatePaymentId;
import static com.exawallet.model.Wallet.isPaymentIdValid;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.presenters.operations.QRCodeOperation.generateQRCode;
import static java.lang.String.valueOf;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class ReceivePresenter extends BaseSyncWalletPresenter<IReceiveView> {
    private String mPaymentId = null;

    @Override
    public void onResume() {
        super.onResume();
        mView.setAddress(mWalletMeta.getAddress());
        mView.showQRImage(mWalletMeta.getQRAddress());
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(final QRAddress event) {
        mView.showQRImage(event.getBitmap());
    }

    public void onCopyButtoon() {
        ((ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", getAddressWithPaimentId()));
        makeText(mActivity, R.string.wallet_clipboard, Toast.LENGTH_SHORT).show();
    }

    public void onShareButton() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getAddressWithPaimentId());
        intent.setType("text/plain");
        mActivity.startActivity(intent);
    }

    public TextWatcher getPaimentIdWatcher() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable paymentId) {
                if (isEmpty(mPaymentId = valueOf(paymentId)) || isPaymentIdValid(mPaymentId)) {
                    mView.setPaymentIdError(null);
                    generateQRCode(mActivity, getAddressWithPaimentId(), qrResult -> walletManager().setQRAddress(qrResult.getResult()));
                } else {
                    mView.setPaymentIdError(getString(R.string.invalid_payment_id_error));
                }
            }
        };
    }

    private String getAddressWithPaimentId() {
        return mWalletMeta.getAddress().concat(isEmpty(mPaymentId) ? "" : "?tx_payment_id=".concat(mPaymentId));
    }

    public void onPaimentId(TextView paimentIdEditText) {
        paimentIdEditText.setText(generatePaymentId());
    }
}