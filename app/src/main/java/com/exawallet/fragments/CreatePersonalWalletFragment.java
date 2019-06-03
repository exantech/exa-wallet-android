package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.CreatePersonalWalletPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.CREATE_PRIVATE_WALLET;

public class CreatePersonalWalletFragment extends CreateWalletFragment<CreatePersonalWalletPresenter> {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BACK_PATH.setScreen(CREATE_PRIVATE_WALLET);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_create_personal_wallet;
    }

    @Override
    CreatePersonalWalletPresenter createPresenter() {
        return new CreatePersonalWalletPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_create_personal_wallet, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new CreatePersonalWalletFragment();
    }
}