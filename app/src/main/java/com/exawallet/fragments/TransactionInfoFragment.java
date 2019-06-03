package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.model.TransactionInfo;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.TransactionInfoPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.ITransactionInfoView;

import java.util.Date;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.exawallet.model.TransactionInfo.Direction.Direction_Out;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.TRANSACTION_INFO;
import static com.exawallet.utils.Utils.*;

public class TransactionInfoFragment extends BaseSyncWalletFragment<TransactionInfoPresenter, ITransactionInfoView, BaseHomeToolbar> implements ITransactionInfoView {
    @BindView(R.id.icon)
    View mIcon;

    @BindView(R.id.amount)
    TextView mAmount;

    @BindView(R.id.datetime)
    TextView mDateTime;

    @BindView(R.id.address_layout)
    View mAddressLayout;

    @BindView(R.id.address_hint)
    TextView mAddressHint;

    @BindView(R.id.address)
    TextView mAddress;

    @BindView(R.id.address_copy)
    View mAddressCopy;

    @BindView(R.id.confirmations_layout)
    View mConfirmationsLayout;

    @BindView(R.id.confirmations)
    TextView mConfirmations;

    @BindView(R.id.actual_fee)
    TextView mActualFee;

    @BindView(R.id.note)
    TextView mNote;

    @BindView(R.id.note_layout)
    View mNoteLayout;

    @BindView(R.id.txhash)
    TextView mHash;

    @BindView(R.id.txhash_copy)
    View mHashCopy;

    @BindView(R.id.payment_id_layout)
    View mPaymentIdLayout;

    @BindView(R.id.payment_id)
    TextView mPaymentId;

    @BindView(R.id.payment_id_copy)
    View mPaymentIdCopy;

    @BindView(R.id.txhash_button)
    View mHashButton;

    private TransactionInfo mTransactionInfo;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BACK_PATH.setScreen(TRANSACTION_INFO);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_transaction_info;
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_transaction, (RootActivity) getActivity());
    }

    @Override
    TransactionInfoPresenter createPresenter() {
        return new TransactionInfoPresenter(mTransactionInfo);
    }

    private TransactionInfoFragment setTransactionInfo(TransactionInfo transactionInfo) {
        mTransactionInfo = transactionInfo;
        return this;
    }

    @Override
    public void showTransactionInfo(TransactionInfo transactionInfo) {
        mAmount.setText(formatAmountB(transactionInfo.amount));

        mIcon.setBackgroundResource(transactionInfo.direction == Direction_Out ? R.drawable.ic_sended : R.drawable.ic_received);
        if (isEmpty(transactionInfo.getAddress())) {
            mAddressLayout.setVisibility(GONE);
        } else {
            mAddressLayout.setVisibility(VISIBLE);
            mAddressHint.setText(transactionInfo.direction == Direction_Out ? R.string.to : R.string.from);
            mAddress.setText(transactionInfo.getAddress());
            mAddressCopy.setOnClickListener(view -> mPresenter.onCopyAddress());
        }

        mDateTime.setText(formatDateFull(new Date(transactionInfo.timestamp * 1000)));

        if (transactionInfo.isFailed) {
            mConfirmationsLayout.setVisibility(GONE);
        } else {
            mConfirmationsLayout.setVisibility(VISIBLE);
            mConfirmations.setText(String.valueOf(transactionInfo.confirmations));
        }

        mActualFee.setText(formatAmount(transactionInfo.fee));

        if (isEmpty(transactionInfo.notes)) {
            mNoteLayout.setVisibility(GONE);
        } else {
            mNoteLayout.setVisibility(VISIBLE);
            mNote.setText(transactionInfo.notes);
        }

        mHash.setText(transactionInfo.hash);
        mHashCopy.setOnClickListener(view -> mPresenter.onCopyHash());

        mPaymentIdLayout.setVisibility(isEmptyId(mTransactionInfo.paymentId) ? GONE : VISIBLE);
        mPaymentId.setText(mTransactionInfo.paymentId);
        mPaymentIdCopy.setOnClickListener(view -> mPresenter.onCopyPaymentId());

        mHashButton.setOnClickListener(v -> mPresenter.onGotoTxHash());
    }

    private boolean isEmptyId(String paymentId) {
        return isEmpty(paymentId) || isEmpty(paymentId.trim()) || isFilled(paymentId.trim(), '0');
    }

    public static TransactionInfoFragment newInstance(TransactionInfo transactionInfo) {
        return new TransactionInfoFragment().setTransactionInfo(transactionInfo);
    }
}