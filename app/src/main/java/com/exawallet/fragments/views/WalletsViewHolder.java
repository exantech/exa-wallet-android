package com.exawallet.fragments.views;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.monerowallet.R;

import static android.graphics.Paint.Style.FILL_AND_STROKE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WalletsViewHolder extends RecyclerView.ViewHolder {
    private final Context mContext;

    @BindView(R.id.info)
    View mInfo;

    @BindView(R.id.wallet_color)
    View mColor;

    @BindView(R.id.wallet_name)
    TextView mWalletName;

    @BindView(R.id.personal_wallet)
    View mPersonalWallet;

    @BindView(R.id.multisig_wallet)
    View mMultisigWallet;

    @BindView(R.id.wallet_info)
    TextView mWalletInfo;

    public WalletsViewHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;

        ButterKnife.bind(this, itemView);
    }

    public void setWalletDescription(WalletMeta walletMeta, View.OnClickListener onWalletClickListener) {
        mWalletName.setText(walletMeta.getName());

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(mContext.getResources().getColor(walletMeta.getColor().getValue()));
        drawable.getPaint().setStyle(FILL_AND_STROKE);
        drawable.getPaint().setAntiAlias(true);

        mColor.setBackground(drawable);

        if (null == walletMeta.getSharedMeta()) {
            mPersonalWallet.setVisibility(VISIBLE);
            mMultisigWallet.setVisibility(GONE);
            mWalletInfo.setText(R.string.personal_wallet);
        } else {
            mPersonalWallet.setVisibility(GONE);
            mMultisigWallet.setVisibility(VISIBLE);
            mWalletInfo.setText(R.string.shared_wallet);
        }

        mInfo.setOnClickListener(onWalletClickListener);
    }
}