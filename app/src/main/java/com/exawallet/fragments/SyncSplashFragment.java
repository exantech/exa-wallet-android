package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.SyncSplashPresenter;
import com.exawallet.views.ISyncSplashView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SPLASH;
import static java.lang.String.format;

public class SyncSplashFragment extends BaseFragment<SyncSplashPresenter, ISyncSplashView> implements ISyncSplashView {
    @BindView(R.id.connection_notify)
    View mConnectionView;

    @BindView(R.id.percent_info)
    TextView mPercentInfo;

    @Override
    int getLayout() {
        return R.layout.fragment_sync_splash;
    }

    @Override
    SyncSplashPresenter createPresenter() {
        return new SyncSplashPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BACK_PATH.setScreen(SPLASH);
    }

    @Override
    public void notifyConnection(boolean isConnected) {
        mConnectionView.setVisibility(isConnected ? GONE : VISIBLE);
    }

    public void setProgress(long blocks) {
        mPercentInfo.setText(0 < blocks ? format(getString(R.string.remaining_blocks), blocks) : getString(R.string.sync_complete));
    }

    public static BaseFragment newInstance() {
        return new SyncSplashFragment();
    }
}