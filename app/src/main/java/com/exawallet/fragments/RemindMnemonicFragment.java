package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.RemindMnemonicPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.REMIND_MNEMONIC;

public class RemindMnemonicFragment extends BaseToolbarFragment<RemindMnemonicPresenter, IBaseView, BaseHomeToolbar> implements IBaseView {
    @BindView(R.id.mnemonic)
    GridView mMnemonic;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMnemonic.setAdapter(mPresenter.getMnemonicAdapter());

        BACK_PATH.setScreen(REMIND_MNEMONIC);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_remind_mnemonic;
    }

    @Override
    RemindMnemonicPresenter createPresenter() {
        return new RemindMnemonicPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_mnemonic, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new RemindMnemonicFragment();
    }
}