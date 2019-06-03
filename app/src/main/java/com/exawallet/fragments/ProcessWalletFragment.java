package com.exawallet.fragments;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import com.exawallet.model.WalletColor;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.ProcessWalletPresenter;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IProcessWalletView;

import static android.graphics.Paint.Style.FILL_AND_STROKE;
import static com.exawallet.model.WalletColor.ORANGE;

public abstract class ProcessWalletFragment<P extends ProcessWalletPresenter, V extends IProcessWalletView> extends BaseToolbarFragment<P, V, BaseToolbar> implements IProcessWalletView {
    @BindView(R.id.wallet_color)
    View mWalletColor;

    @BindView(R.id.wallet_color_orange)
    View mOrange;

    @BindView(R.id.wallet_color_blue)
    View mBlue;

    @BindView(R.id.wallet_color_green)
    View mGreen;

    @BindView(R.id.wallet_color_pale)
    View mPale;

    @BindView(R.id.wallet_color_purple)
    View mPurple;

    @BindView(R.id.wallet_color_tangelo)
    View mTangelo;

    @BindView(R.id.wallet_name)
    EditText mWalletName;

    @BindView(R.id.node)
    EditText mNode;

    @BindView(R.id.continue_button)
    View mContinueButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mOrange.setOnClickListener(v -> mPresenter.onOrange());
        mBlue.setOnClickListener(v -> mPresenter.onBlue());
        mGreen.setOnClickListener(v -> mPresenter.onGreen());
        mPale.setOnClickListener(v -> mPresenter.onPale());
        mPurple.setOnClickListener(v -> mPresenter.onPurple());
        mTangelo.setOnClickListener(v -> mPresenter.onTangelo());

        mWalletName.addTextChangedListener(mPresenter.getWalletWatcher());

        setWalletColor(ORANGE);
    }

    public void setWalletColor(WalletColor walletColor) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(walletColor.getValue()));
        drawable.getPaint().setStyle(FILL_AND_STROKE);
        drawable.getPaint().setAntiAlias(true);

        mWalletColor.setBackground(drawable);
    }
}
