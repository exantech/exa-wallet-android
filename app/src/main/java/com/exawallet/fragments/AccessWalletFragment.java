package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.AccessWalletPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.ACCESS_WALLET;

public class AccessWalletFragment extends BaseToolbarFragment<AccessWalletPresenter, IBaseView, BaseToolbar> implements IBaseView {
    @BindView(R.id.create_private_wallet_button)
    View mCreatePersonalWalletButton;

    @BindView(R.id.create_multisig_wallet_button)
    View mCreateMultisigWalletButton;

    @BindView(R.id.join_multisig_wallet_button)
    View mJoinMultisigWalletButton;

    @BindView(R.id.restore_wallet_button)
    View mRestoreWalletButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCreatePersonalWalletButton.setOnClickListener(v -> mPresenter.onCreatePersonalWallet());
        mCreateMultisigWalletButton.setOnClickListener(v -> mPresenter.onCreateMultisigWallet());
        mJoinMultisigWalletButton.setOnClickListener(v -> mPresenter.onJoinWallet());
        mRestoreWalletButton.setOnClickListener(v -> mPresenter.onRestoreWallet());

        BACK_PATH.setScreen(ACCESS_WALLET);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_access_wallet;
    }

    @Override
    AccessWalletPresenter createPresenter() {
        return new AccessWalletPresenter();
    }

    @Override
    BaseToolbar createToolbar(View view) {
        return BACK_PATH.canBack() ? new BaseHomeToolbar(view, R.string.action_select_action, (RootActivity) getActivity()) : new BaseToolbar(view, R.string.action_select_action, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new AccessWalletFragment();
    }
}