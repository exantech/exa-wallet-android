package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.ConfirmMnemonicPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.CONFIRM_MNEMONIC;

public class ConfirmMnemonicFragment extends BaseToolbarFragment<ConfirmMnemonicPresenter, IBaseView, BaseHomeToolbar> implements IBaseView {
    @BindView(R.id.mnemonic)
    GridView mMnemonic;

    @BindView(R.id.secrets)
    GridView mSecrets;

    @BindView(R.id.continue_button)
    View mButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMnemonic.setAdapter(mPresenter.getMnemonicAdapter());

        mSecrets.setAdapter(mPresenter.getSecretAdapter());
        mSecrets.setOnItemClickListener((parent, view1, position, id) -> mPresenter.getOnItemClickListener(position));

        setOnClickListener(mButton, () -> mPresenter.onSkipConfirm());

        BACK_PATH.setScreen(CONFIRM_MNEMONIC);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_confirm_mnemonic;
    }

    @Override
    ConfirmMnemonicPresenter createPresenter() {
        return new ConfirmMnemonicPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_mnemonic, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new ConfirmMnemonicFragment();
    }
}