package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.SuccessMnemonicPresenter;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SUCCESS_MNEMONIC;

public class SuccessMnemonicFragment extends BaseToolbarFragment<SuccessMnemonicPresenter, IBaseView, BaseToolbar> implements IBaseView {
    @BindView(R.id.button_continue)
    TextView mContinueButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setOnClickListener(mContinueButton, () -> mPresenter.onContinueButton());

        BACK_PATH.setScreen(SUCCESS_MNEMONIC);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_success_mnemonic;
    }

    @Override
    SuccessMnemonicPresenter createPresenter() {
        return new SuccessMnemonicPresenter();
    }

    @Override
    BaseToolbar createToolbar(View view) {
        return new BaseToolbar(view, R.string.action_mnemonic, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new SuccessMnemonicFragment();
    }
}