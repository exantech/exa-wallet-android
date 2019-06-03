package com.exawallet.fragments.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.exawallet.model.TransactionInfo;
import com.exawallet.monerowallet.R;

import java.util.Date;

import static com.exawallet.utils.Utils.formatAmount;
import static com.exawallet.utils.Utils.formatDateFull;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.info)
    View mInfo;

    @BindView(R.id.icon)
    View mIcon;

    @BindView(R.id.status)
    View mStatus;

    @BindView(R.id.amount)
    TextView mAmount;

    @BindView(R.id.datetime)
    TextView mDateTime;

    public TransactionViewHolder(View viewGroup) {
        super(viewGroup);

        ButterKnife.bind(this, viewGroup);
    }

    public void setTransactionDescription(TransactionInfo transactionInfo, View.OnClickListener infoClickListener) {
        mIcon.setBackgroundResource(transactionInfo.direction == TransactionInfo.Direction.Direction_Out ? R.drawable.ic_sended : R.drawable.ic_received);
        mAmount.setText(formatAmount(transactionInfo.amount));
        mDateTime.setText(formatDateFull(new Date(transactionInfo.timestamp * 1000)));

        if (transactionInfo.isFailed) {
            mStatus.setVisibility(View.VISIBLE);
            mStatus.setBackgroundResource(R.drawable.ic_declined_gray);
        } else if (transactionInfo.isPending) {
            mStatus.setVisibility(View.VISIBLE);
            mStatus.setBackgroundResource(R.drawable.ic_clock_blue);
        } else {
            mStatus.setVisibility(View.GONE);
        }

        mInfo.setOnClickListener(infoClickListener);
    }
}