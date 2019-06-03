package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.SettingsPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IBaseView;

import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SETTINGS;

public class SettingsFragment extends BaseToolbarFragment<SettingsPresenter, IBaseView, BaseToolbar> implements IBaseView {
    @BindView(R.id.setup_pin)
    View mSetupPin;

    @BindView(R.id.spy_protection_check_box)
    CheckBox mSpyProtection;

    @BindView(R.id.about)
    View mAbout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSpyProtection.setChecked(APP_CONTEXT.useSpyProtection());

        mSetupPin.setOnClickListener(view1 -> mPresenter.onSetupPin());
        mSpyProtection.setOnClickListener(view1 -> mPresenter.onSpyProtection(mSpyProtection));
        mAbout.setOnClickListener(view1 -> mPresenter.onAbout());


        BACK_PATH.setScreen(SETTINGS);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    SettingsPresenter createPresenter() {
        return new SettingsPresenter();
    }

    @Override
    BaseToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_settings, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new SettingsFragment();
    }
}
