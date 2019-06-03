package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.ShowMnemonicPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SHOW_MNEMONIC;

public class ShowMnemonicFragment extends BaseToolbarFragment<ShowMnemonicPresenter, IBaseView, BaseHomeToolbar> implements IBaseView {
    @BindView(R.id.mnemonic)
    GridView mMnemonic;

    @BindView(R.id.button_continue)
    TextView mContinueButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMnemonic.setAdapter(mPresenter.getMnemonicAdapter());

        setOnClickListener(mContinueButton, () -> mPresenter.onContinueButton());

        BACK_PATH.setScreen(SHOW_MNEMONIC);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_show_mnemonic;
    }

    @Override
    ShowMnemonicPresenter createPresenter() {
        return new ShowMnemonicPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_mnemonic, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new ShowMnemonicFragment();
    }
}