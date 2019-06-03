package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.BuildConfig;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.AboutPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.ABOUT;

public class AboutFragment extends BaseToolbarFragment<AboutPresenter, IBaseView, BaseHomeToolbar> implements IBaseView {
    @BindView(R.id.version)
    TextView mVersion;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVersion.setText(BuildConfig.VERSION_NAME);

        BACK_PATH.setScreen(ABOUT);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_about;
    }

    @Override
    AboutPresenter createPresenter() {
        return new AboutPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_about, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new AboutFragment();
    }
}
