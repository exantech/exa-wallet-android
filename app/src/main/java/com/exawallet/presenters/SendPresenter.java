package com.exawallet.presenters;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.listeners.OnQRScanListener;
import com.exawallet.utils.SimpleTextWatcher;
import com.exawallet.views.ISendView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.Wallet.generatePaymentId;
import static com.exawallet.model.Wallet.isPaymentIdValid;
import static com.exawallet.presenters.operations.CreateTxProposalOperation.createTxProposals;
import static com.exawallet.presenters.operations.SendTransactionOperation.sendTransaction;
import static com.exawallet.utils.Screens.BEHAVIOUR;
import static com.exawallet.utils.Screens.SEND;
import static com.exawallet.utils.Utils.parseAmount;
import static com.exawallet.utils.Utils.validateAmount;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.String.valueOf;

public class SendPresenter extends BaseSyncWalletPresenter<ISendView> implements OnQRScanListener {
    private long mAmount;
    private String mPaymentId;
    private String mAddress;
    private boolean mAddressAccepted;
    private boolean mAmountAccepted;
    private boolean mDescriptionAccepted;
    private String mPaymentNote;

    @Override
    public void onResume() {
        super.onResume();
        if (null != SEND.popObject(BEHAVIOUR)) {
            mActivity.goBack();
        }

        if (null != mWalletMeta.getSharedMeta() && mWalletMeta.getSharedMeta().needRequestOutputs()) {
            mView.showErrorHint(getString(R.string.no_sync_wallet_notify));
        }

        mView.setPaymentNoteHint(getString((mDescriptionAccepted = null == mWalletMeta.getSharedMeta()) ? R.string.prompt_payment_note_optional : R.string.prompt_payment_note));
    }

    public void onSend() {
//        if (isEmpty(mPaymentId)) {
//            mPaymentId = "0000000000000000";
//            mPaymentId = "";
//        }

        SEND.pushObject(BEHAVIOUR, "back");

        if (null == mWalletMeta.getSharedMeta()) {
            sendTransaction(mAddress, isEmpty(mPaymentId) ? "0000000000000000" : mPaymentId, mPaymentNote, mAmount, mActivity);
        } else {
            createTxProposals(mAddress, isEmpty(mPaymentId) ? "" : mPaymentId, mPaymentNote, mAmount, mWallet.getPublicMultisigSignerKey(), mActivity);
        }
    }

    public TextWatcher getAddressWatcher() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                final String address = valueOf(editable).trim();

                if (address.contains("?tx_payment_id=")) {
                    String[] addressFiels = address.split("\\?tx_payment_id=");
                    mAddress = addressFiels[0];
                    mPaymentId = addressFiels[1];
                    mView.setAddressTo(mAddress);
                    mView.setPaimentId(mPaymentId);
                }

                mView.setAddressError(!(mAddressAccepted = !isEmpty(mAddress) && mAddress.length() == 95 && checkAddressSymbols()) ? getString(R.string.invalid_address_error) : null);
                updateSendButtonEnabled();
            }

            private boolean checkAddressSymbols() {
                for (int index = 2; index < mAddress.length(); index++) {
                    char charAt = mAddress.toLowerCase().charAt(index);
                    if ((!isDigit(charAt) && !isLetter(charAt))) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private void updateSendButtonEnabled() {
        if (null != mView) {
            mView.enableSendButton(mAddressAccepted && mAmountAccepted && mDescriptionAccepted && (null == mWalletMeta.getSharedMeta() || !mWalletMeta.getSharedMeta().needRequestOutputs()));
        }
    }

    public TextWatcher getAmountWatcher(final EditText amountEditText) {
        return new SimpleTextWatcher() {
            EditText _amountEditText = amountEditText;

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String validAmount = validateAmount(valueOf(editable));

                    if (!validAmount.equals(valueOf(editable))) {
                        _amountEditText.setText(validAmount);
                        _amountEditText.setSelection(validAmount.length());
                    }

                    mView.setAmountError(!(mAmountAccepted = (mAmount = parseAmount(valueOf(editable))) < mWallet.getUnlockedBalance() && 0 < mAmount) ? getString(R.string.invalid_amount_error) : null);
                    updateSendButtonEnabled();
                } catch (Exception e) {
                    mView.setAmountError(e.getLocalizedMessage());
                }
            }
        };
    }

    public TextWatcher getPaimentIdWatcher() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable paymentId) {
                mView.setPaymentIdError(isEmpty(mPaymentId = valueOf(paymentId)) || isPaymentIdValid(mPaymentId) ? null : getString(R.string.invalid_payment_id_error));
                updateSendButtonEnabled();
            }
        };
    }

    public void onPaimentId(TextView paimentIdEditText) {
        paimentIdEditText.setText(generatePaymentId());
    }

    public TextWatcher getPaimentNoteWatcher() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable paymentNote) {
                mView.setDescriptionError((mDescriptionAccepted = !isEmpty(mPaymentNote = valueOf(paymentNote)) || null == mWalletMeta.getSharedMeta()) ? null : getString(R.string.invalid_transaction_description));

                updateSendButtonEnabled();
            }
        };
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        mActivity.setOnQRScanListener(null);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (null != result && null != result.getContents()) {
            mView.setAddressTo(result.getContents());
            return false;
        } else {
            return true;
        }
    }

    public void scanQRCode() {
        mActivity.setOnQRScanListener(this);
        new IntentIntegrator(mActivity).initiateScan();
    }
}