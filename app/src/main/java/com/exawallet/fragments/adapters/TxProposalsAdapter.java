package com.exawallet.fragments.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.exawallet.RootActivity;
import com.exawallet.fragments.TxProposalsMetaFragment;
import com.exawallet.fragments.views.TxProposalsViewHolder;
import com.exawallet.model.meta.SharedMeta;
import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.monerowallet.R;

import java.util.LinkedList;
import java.util.List;

import static com.exawallet.model.meta.TxProposalStage.*;
import static java.util.Collections.sort;

public class TxProposalsAdapter extends RecyclerView.Adapter<TxProposalsViewHolder> {
    private List<TxProposalsMeta> mTxProposalsMetaList;
    private final RootActivity mActivity;
    private String mPublicMultisigSignerKey;
    private final SharedMeta mSharedMeta;

    public TxProposalsAdapter(RootActivity mActivity, String publicMultisigSignerKey, SharedMeta sharedMeta) {
        this.mActivity = mActivity;
        mPublicMultisigSignerKey = publicMultisigSignerKey;
        this.mSharedMeta = sharedMeta;
    }

    @NonNull
    @Override
    public TxProposalsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new TxProposalsViewHolder(mActivity, mPublicMultisigSignerKey, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_tx_proposals_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TxProposalsViewHolder holder, int position) {
        TxProposalsMeta txProposalsMeta = mTxProposalsMetaList.get(position);
        holder.setTransactionDescription(txProposalsMeta, mSharedMeta, v -> mActivity.show(TxProposalsMetaFragment.newInstance(txProposalsMeta)));
    }

    @Override
    public int getItemCount() {
        return null == mTxProposalsMetaList ? 0 : mTxProposalsMetaList.size();
    }

    public void setItems(List<TxProposalsMeta> items) {
        this.mTxProposalsMetaList = new LinkedList<>();

        for (TxProposalsMeta txProposalsMeta : items) {
            if ((CREATE_TX_PROPOSALS == txProposalsMeta.getStage() || SEND_TX_PROPOSALS_DECISION == txProposalsMeta.getStage()) || EXPECT_TX_PROPOSALS_DECISION == txProposalsMeta.getStage() ||
                    (TX_PROPOSALS_DECISION_ACCEPTED == txProposalsMeta.getStage() && mSharedMeta.getSigners() > txProposalsMeta.getApprovalsCount() + txProposalsMeta.getRejectsCount())) {
                mTxProposalsMetaList.add(txProposalsMeta);
            }
        }

        sort(this.mTxProposalsMetaList);
        notifyDataSetChanged();
    }
}