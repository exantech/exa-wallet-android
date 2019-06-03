package com.exawallet.fragments.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.exawallet.RootActivity;
import com.exawallet.model.meta.SharedMeta;
import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.monerowallet.R;

import java.util.Date;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.meta.TxProposalStage.*;
import static com.exawallet.utils.Utils.formatAmount;
import static com.exawallet.utils.Utils.formatDateFull;

public class TxProposalsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.info)
    View mInfo;

    @BindView(R.id.icon)
    View mIcon;

    @BindView(R.id.amount)
    TextView mAmount;

    @BindView(R.id.datetime)
    TextView mDateTime;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(R.id.approvals)
    TextView mApprovals;

    @BindView(R.id.declined)
    TextView mDeclined;

    private final RootActivity mActivity;
    private String mPublicMultisigSignerKey;

    public TxProposalsViewHolder(RootActivity activity, String publicMultisigSignerKey, View viewGroup) {
        super(viewGroup);
        this.mActivity = activity;
        mPublicMultisigSignerKey = publicMultisigSignerKey;

        ButterKnife.bind(this, viewGroup);
    }

    public void setTransactionDescription(TxProposalsMeta txProposalsMeta, SharedMeta sharedMeta, View.OnClickListener infoClickListener) {
        mAmount.setText(formatAmount(txProposalsMeta.getAmount()));
        mDateTime.setText(formatDateFull(new Date(txProposalsMeta.getTimestamp())));

        mIcon.setBackgroundResource(CREATE_TX_PROPOSALS == txProposalsMeta.getStage() || SEND_TX_PROPOSALS_DECISION == txProposalsMeta.getStage() ? R.drawable.ic_tx_proposals_wait : EXPECT_TX_PROPOSALS_DECISION == txProposalsMeta.getStage() ? R.drawable.ic_tx_proposals_expect : txProposalsMeta.isApproved(mPublicMultisigSignerKey) ? R.drawable.ic_tx_proposals_ok : R.drawable.ic_tx_proposals_declined);

        if (!isEmpty(txProposalsMeta.getDescription())) {
            mDescription.setText(txProposalsMeta.getDescription());
        }

        mApprovals.setText(String.format(mActivity.getString(R.string.approvals_from), txProposalsMeta.getApprovalsCount(), sharedMeta.getSigners()));
        mDeclined.setText(String.format(mActivity.getString(R.string.rejects_from), txProposalsMeta.getRejectsCount(), sharedMeta.getSigners()));

        mInfo.setOnClickListener(infoClickListener);
    }
}
