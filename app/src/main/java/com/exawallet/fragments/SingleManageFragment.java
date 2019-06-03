package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.SingleManagePresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SINGLE_MANAGE;

public class SingleManageFragment extends BaseToolbarFragment<SingleManagePresenter, IBaseView, BaseHomeToolbar> implements IBaseView {
    @BindView(R.id.edit_wallet)
    View mEditWallet;

    @BindView(R.id.change_password)
    View mChangePassword;

    @BindView(R.id.delete_wallet)
    View mDeleteWallet;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditWallet.setOnClickListener(v -> mPresenter.onEditWallet());
        mChangePassword.setOnClickListener(v -> mPresenter.onChangePassword());
        mDeleteWallet.setOnClickListener(v -> mPresenter.onDeleteWallet());

        BACK_PATH.setScreen(SINGLE_MANAGE);
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_manage_wallet, (RootActivity) getActivity());
    }

    @Override
    int getLayout() {
        return R.layout.fragment_manage_single;
    }

    @Override
    SingleManagePresenter createPresenter() {
        return new SingleManagePresenter();
    }

    public static BaseFragment newInstance() {
        return new SingleManageFragment();
    }
}
