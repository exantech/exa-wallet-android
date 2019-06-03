package com.exawallet.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;
import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.monerowallet.R;
import com.exawallet.views.ITxProposalsMetaView;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.widget.Toast.makeText;

public class TxProposalsMetaPresenter extends BaseSyncWalletPresenter<ITxProposalsMetaView> {
    private final TxProposalsMeta mTxProposalsMeta;

    public TxProposalsMetaPresenter(TxProposalsMeta txProposalsMeta) {
        this.mTxProposalsMeta = txProposalsMeta;
    }

    @Override
    public void onResume() {
        super.onResume();
        mView.showTxProposalsMeta(mTxProposalsMeta);

        mView.enableButtons(!mWalletMeta.getSharedMeta().needRequestOutputs());
    }

    public void onCopyAddress() {
        ((ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("address", mTxProposalsMeta.getDestinationAddress()));
        makeText(mActivity, R.string.address_clipboard, Toast.LENGTH_SHORT).show();
    }

    public void onApproval() {
        mTxProposalsMeta.approve(mWallet.getPublicMultisigSignerKey());

        mActivity.goBack();
    }

    public void onReject() {
        mTxProposalsMeta.reject(mWallet.getPublicMultisigSignerKey());

        mActivity.goBack();
    }
}