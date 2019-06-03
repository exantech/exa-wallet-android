package com.exawallet.fragments.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.exawallet.RootActivity;
import com.exawallet.dialogs.EnterPasswordDialog;
import com.exawallet.dialogs.ErrorDialog;
import com.exawallet.dialogs.TwoButtonDialog;
import com.exawallet.fragments.views.WalletsViewHolder;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.monerowallet.R;

import java.io.IOException;
import java.util.List;

import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.presenters.operations.LoadContentOperation.loadContent;
import static com.exawallet.presenters.operations.OpenWalletOperation.openWallet;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Utils.selectWalletScreen;

public class WalletsAdapter extends RecyclerView.Adapter<WalletsViewHolder> {
    private final RootActivity mActivity;
    private final List<WalletMeta> mWallets;

    public WalletsAdapter(RootActivity mActivity, List<WalletMeta> walletMetas) {
        this.mActivity = mActivity;
        this.mWallets = walletMetas;
    }

    @NonNull
    @Override
    public WalletsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new WalletsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_wallet_item, viewGroup, false), mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletsViewHolder holder, int position) {
        WalletMeta walletMeta = mWallets.get(position);
        holder.setWalletDescription(walletMeta, v -> {
                    if (null != walletManager().getWallet() && walletManager().getWallet().getName().equals(walletMeta.getFileName())) {
                        selectWalletScreen(walletMeta, mActivity);
                    } else {
                        if (walletManager().walletExists(walletMeta.getFile())) {
                            if (walletMeta.isEmptyPassword()) {
                                openWallet("", walletMeta, mActivity);
                            } else {
                                new EnterPasswordDialog(mActivity, password -> openWallet(password, walletMeta, mActivity)).show();
                            }
                        } else {
                            new TwoButtonDialog(mActivity, mActivity.getString(R.string.warning), mActivity.getString(R.string.delete_wallet_notify)) {
                                @Override
                                protected void onButtonOkClick() {
                                    try {
                                        walletMeta.deleted();
                                        BACK_PATH.clear();
                                        dismiss();
                                        loadContent(mActivity);
                                    } catch (IOException e) {
                                        new ErrorDialog(mActivity, mActivity.getString(R.string.error), e.getLocalizedMessage()).show();
                                    }
                                }
                            }.show();
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return null == mWallets ? 0 : mWallets.size();
    }
}