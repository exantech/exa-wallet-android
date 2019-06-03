package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.SplashPresenter;
import com.exawallet.views.IBaseView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SPLASH;

public class SplashFragment extends BaseFragment<SplashPresenter, IBaseView> implements IBaseView {
    @Override
    int getLayout() {
        return R.layout.fragment_splash;
    }

    @Override
    SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BACK_PATH.setScreen(SPLASH);
    }

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }
}