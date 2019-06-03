package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.JoinSharedWalletPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IRecreateWalletView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.INVITE_CODE;
import static com.exawallet.utils.Screens.JOIN_SHARED_WALLET;

public class JoinSharedWalletFragment extends CreateWalletFragment<JoinSharedWalletPresenter> implements IRecreateWalletView {
    private String mInviteCode;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInviteCode = (String) JOIN_SHARED_WALLET.popObject(INVITE_CODE);

        BACK_PATH.setScreen(JOIN_SHARED_WALLET);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_join_multisig_wallet;
    }

    @Override
    JoinSharedWalletPresenter createPresenter() {
        return new JoinSharedWalletPresenter(mInviteCode);
    }

    @Override
    BaseToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_insert_invite_code, (RootActivity) getActivity());
    }

    private BaseFragment setParams(String inviteCode) {
        mInviteCode = inviteCode;

        return this;
    }

    public static BaseFragment newInstance() {
        return new JoinSharedWalletFragment();
    }

    public static BaseFragment newInstance(String inviteCode) {
        return new JoinSharedWalletFragment().setParams(inviteCode);
    }
}