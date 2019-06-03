package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.EditWalletPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.IEditWalletView;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.EDIT_WALLET;
import static java.lang.String.valueOf;

public class EditWalletFragment extends ProcessWalletFragment<EditWalletPresenter, IEditWalletView> implements IEditWalletView {
    @BindView(R.id.personal_wallet)
    View mPersonalWallet;

    @BindView(R.id.multisig_wallet)
    View mMultisigWallet;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListener(mContinueButton, () -> {
            Editable walletName = mWalletName.getText();
            Editable node = mNode.getText();

            if (!isEmpty(walletName)) {
                mPresenter.onEditWallet(valueOf(walletName), valueOf(node));
            }
        });

        BACK_PATH.setScreen(EDIT_WALLET);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_edit_wallet;
    }

    @Override
    EditWalletPresenter createPresenter() {
        return new EditWalletPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_edit_wallet, (RootActivity) getActivity());
    }

    @Override
    public void showWalletType(boolean isPersonal) {
        if (isPersonal) {
            mPersonalWallet.setVisibility(VISIBLE);
            mMultisigWallet.setVisibility(GONE);
        } else {
            mPersonalWallet.setVisibility(GONE);
            mMultisigWallet.setVisibility(VISIBLE);
        }
    }

    @Override
    public void showWalletName(String name) {
        mWalletName.setText(name);
    }

    @Override
    public void showWalletNode(String daemonAddress) {
        mNode.setText(daemonAddress);
    }

    @Override
    public void checkWalletName(boolean matches) {
        mContinueButton.setEnabled(matches);
    }

    public static BaseFragment newInstance() {
        return new EditWalletFragment();
    }
}