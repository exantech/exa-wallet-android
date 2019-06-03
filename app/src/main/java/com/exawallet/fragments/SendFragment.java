package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.SendPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.WalletManageToolbar;
import com.exawallet.views.ISendView;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SEND;

public class SendFragment extends BaseSyncWalletFragment<SendPresenter, ISendView, BaseHomeToolbar> implements ISendView {
    @BindView(R.id.address_layout)
    TextInputLayout mAddressLayout;

    @BindView(R.id.address)
    EditText mAddressEditText;

    @BindView(R.id.qr_code)
    View mQrCodeView;

    @BindView(R.id.amount_layout)
    TextInputLayout mAmountLayout;

    @BindView(R.id.amount)
    EditText mAmountEditText;

    @BindView(R.id.payment_id_layout)
    TextInputLayout mPaimentIdLayout;

    @BindView(R.id.payment_id)
    EditText mPaimentIdEditText;

    @BindView(R.id.payment_note_layout)
    TextInputLayout mPaimentNoteLayout;

    @BindView(R.id.payment_note)
    EditText mPaimentNoteEditText;

    @BindView(R.id.payment_id_button)
    View mPaimentIdButton;

    @BindView(R.id.error_notify)
    TextView mErrorNotify;

    @BindView(R.id.send_button)
    View mSendButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddressEditText.addTextChangedListener(mPresenter.getAddressWatcher());
        mQrCodeView.setOnClickListener(v -> mPresenter.scanQRCode());
        mAmountEditText.addTextChangedListener(mPresenter.getAmountWatcher(mAmountEditText));
        mPaimentIdEditText.addTextChangedListener(mPresenter.getPaimentIdWatcher());
        mPaimentNoteEditText.addTextChangedListener(mPresenter.getPaimentNoteWatcher());

        mPaimentIdButton.setOnClickListener(v -> mPresenter.onPaimentId(mPaimentIdEditText));

        mSendButton.setOnClickListener(v -> mPresenter.onSend());

        BACK_PATH.setScreen(SEND);
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new WalletManageToolbar(view, R.string.action_send, (RootActivity) getActivity()) {

            @Override
            protected void onButton() {
                ((RootActivity) getActivity()).show(WalletManageFragment.newInstance());
            }
        };
    }

    @Override
    int getLayout() {
        return R.layout.fragment_send;
    }

    @Override
    SendPresenter createPresenter() {
        return new SendPresenter();
    }

    @Override
    public void setAddressError(String message) {
        setLayoutError(message, mAddressLayout);
    }

    @Override
    public void setAmountError(String message) {
        setLayoutError(message, mAmountLayout);
    }

    @Override
    public void setPaymentIdError(String message) {
        setLayoutError(message, mPaimentIdLayout);
    }

    @Override
    public void setDescriptionError(String message) {
        setLayoutError(message, mPaimentNoteLayout);
    }

    private void setLayoutError(String message, TextInputLayout textInputLayout) {
        textInputLayout.setError(message);
        textInputLayout.setErrorEnabled(!isEmpty(message));
    }

    @Override
    public void setAddressTo(String address) {
        mAddressEditText.setText(address);
    }

    @Override
    public void setPaimentId(String paimentId) {
        mPaimentIdEditText.setText(paimentId);
    }

    @Override
    public void showErrorHint(String string) {
        mErrorNotify.setText(string);
    }

    @Override
    public void setPaymentNoteHint(String paymentNoteHint) {
        mPaimentNoteLayout.setHint(paymentNoteHint);
    }

    public void enableSendButton(boolean isEnable) {
        if (null != mSendButton) {
            mSendButton.setEnabled(isEnable);
        }
    }

    public static SendFragment newInstance() {
        return new SendFragment();
    }
}