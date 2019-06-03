package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.TxProposalsMetaPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.ITxProposalsMetaView;

import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.exawallet.model.meta.TxProposalStage.EXPECT_TX_PROPOSALS_DECISION;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.TXPROPOSALS_META;
import static com.exawallet.utils.Utils.*;

public class TxProposalsMetaFragment extends BaseSyncWalletFragment<TxProposalsMetaPresenter, ITxProposalsMetaView, BaseHomeToolbar> implements ITxProposalsMetaView {
    @BindView(R.id.icon)
    View mIcon;

    @BindView(R.id.amount)
    TextView mAmount;

    @BindView(R.id.datetime)
    TextView mDateTime;

    @BindView(R.id.address_hint)
    TextView mAddressHint;

    @BindView(R.id.address)
    TextView mAddress;

    @BindView(R.id.address_copy)
    View mAddressCopy;

    @BindView(R.id.approvals)
    TextView mApprovals;

    @BindView(R.id.rejects)
    TextView mRejects;

    @BindView(R.id.actual_fee_layout)
    View mActualFeeLayout;

    @BindView(R.id.actual_fee)
    TextView mActualFee;

    @BindView(R.id.note)
    TextView mNote;

    @BindView(R.id.note_layout)
    View mNoteLayout;

    @BindView(R.id.approval_button)
    View mApprovalButton;

    @BindView(R.id.reject_button)
    View mRejectButton;

    private TxProposalsMeta mTxProposalsMeta;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BACK_PATH.setScreen(TXPROPOSALS_META);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_txproposals_meta;
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_txproposals, (RootActivity) getActivity());
    }

    @Override
    TxProposalsMetaPresenter createPresenter() {
        return new TxProposalsMetaPresenter(mTxProposalsMeta);
    }

    @Override
    public void showTxProposalsMeta(TxProposalsMeta txProposalsMeta) {
        mAmount.setText(formatAmountB(txProposalsMeta.getAmount()));

        mIcon.setBackgroundResource(R.drawable.ic_sended);
        mAddressHint.setText(R.string.to);
        mAddress.setText(txProposalsMeta.getDestinationAddress());
        mAddressCopy.setOnClickListener(view -> mPresenter.onCopyAddress());

        mDateTime.setText(formatDateFull(new Date(txProposalsMeta.getTimestamp())));

        mApprovals.setText(String.valueOf(txProposalsMeta.getApprovalsCount()));
        mRejects.setText(String.valueOf(txProposalsMeta.getRejectsCount()));

        mActualFeeLayout.setVisibility(0 < txProposalsMeta.getFee() ? VISIBLE : GONE);
        mActualFee.setText(formatAmount(txProposalsMeta.getFee()));

        mApprovalButton.setVisibility(EXPECT_TX_PROPOSALS_DECISION == mTxProposalsMeta.getStage() ? VISIBLE : GONE);
        mApprovalButton.setOnClickListener(v -> mPresenter.onApproval());

        mRejectButton.setVisibility(EXPECT_TX_PROPOSALS_DECISION == mTxProposalsMeta.getStage() ? VISIBLE : GONE);
        mRejectButton.setOnClickListener(v -> mPresenter.onReject());

        mNote.setText(txProposalsMeta.getDescription());
    }

    @Override
    public void enableButtons(boolean enable) {
        mApprovalButton.setEnabled(enable);
        mRejectButton.setEnabled(enable);
    }

    private TxProposalsMetaFragment setTxProposalsMeta(TxProposalsMeta txProposalsMeta) {
        mTxProposalsMeta = txProposalsMeta;
        return this;
    }

    public static BaseFragment newInstance(TxProposalsMeta txProposalsMeta) {
        return new TxProposalsMetaFragment().setTxProposalsMeta(txProposalsMeta);
    }
}