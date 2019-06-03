package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.WalletManagePresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.IWalletManageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.MANAGE_WALLET;

public class WalletManageFragment extends BaseSyncWalletFragment<WalletManagePresenter, IWalletManageView, BaseHomeToolbar> implements IWalletManageView {
    @BindView(R.id.edit_wallet)
    View mEditWallet;

    @BindView(R.id.remind_mnemonic)
    View mRemindMnemonic;

    @BindView(R.id.change_password)
    View mChangePassword;

    @BindView(R.id.rescan)
    View mRescan;

    @BindView(R.id.change_wallet_type_layout)
    View mChangeWalletTypeLayout;

    @BindView(R.id.change_wallet_type)
    TextView mChangeWalletType;

    @BindView(R.id.drop_imported_layout)
    View mDropImportedLayout;

    @BindView(R.id.drop_imported)
    TextView mDropImported;

    @BindView(R.id.delete_wallet)
    View mDeleteWallet;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditWallet.setOnClickListener(v -> mPresenter.onEditWallet());
        mRemindMnemonic.setOnClickListener(v -> mPresenter.onRemindMnemonic());
        mChangePassword.setOnClickListener(v -> mPresenter.onChangePassword());
        mRescan.setOnClickListener(v -> mPresenter.onRescan());
        mDeleteWallet.setOnClickListener(v -> mPresenter.onDeleteWallet());
        mChangeWalletType.setOnClickListener(v -> mPresenter.onChangeWalletType(mChangeWalletType));
        mDropImported.setOnClickListener(v -> mPresenter.onDropImported());

        BACK_PATH.setScreen(MANAGE_WALLET);
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_manage_wallet, (RootActivity) getActivity());
    }

    @Override
    int getLayout() {
        return R.layout.fragment_manage;
    }

    @Override
    WalletManagePresenter createPresenter() {
        return new WalletManagePresenter();
    }

    @Override
    public void setMnemonic(boolean hasSeed) {
        mRemindMnemonic.setVisibility(hasSeed ? VISIBLE : GONE);
    }

    @Override
    public void setChangeMode(boolean isDebug, boolean isSharedReady) {
        mChangeWalletTypeLayout.setVisibility(isDebug ? VISIBLE : GONE);
        mChangeWalletType.setText(isSharedReady ? R.string.action_set_personal : R.string.action_set_shared);

        mDropImportedLayout.setVisibility(isDebug ? VISIBLE : GONE);
    }

    public static BaseFragment newInstance() {
        return new WalletManageFragment();
    }
}