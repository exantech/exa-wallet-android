package com.exawallet.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.exawallet.RootActivity;
import com.exawallet.fragments.TransactionInfoFragment;
import com.exawallet.fragments.views.TransactionViewHolder;
import com.exawallet.model.TransactionInfo;
import com.exawallet.monerowallet.R;

import java.util.List;

import static java.util.Collections.sort;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionViewHolder> {
    private List<TransactionInfo> mTransactionInfos;
    private final RootActivity mActivity;

    public TransactionsAdapter(RootActivity activity) {
        mActivity = activity;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new TransactionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_transaction_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder transactionViewHolder, int position) {
        TransactionInfo transactionInfo = mTransactionInfos.get(position);
        transactionViewHolder.setTransactionDescription(transactionInfo, v -> mActivity.show(TransactionInfoFragment.newInstance(transactionInfo)));
    }

    @Override
    public int getItemCount() {
        return null == mTransactionInfos ? 0 : mTransactionInfos.size();
    }

    public void setItems(List<TransactionInfo> items) {
        this.mTransactionInfos = items;
        sort(this.mTransactionInfos);
        notifyDataSetChanged();
    }
}