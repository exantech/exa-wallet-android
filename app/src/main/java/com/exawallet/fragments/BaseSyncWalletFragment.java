package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.BindView;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.BaseSyncWalletPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.ISynchronizationView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

abstract class BaseSyncWalletFragment<P extends BaseSyncWalletPresenter, V extends ISynchronizationView, T extends BaseHomeToolbar> extends BaseToolbarFragment<P, V, T> implements ISynchronizationView {
    @BindView(R.id.connection_notify)
    View mConnectionView;

    @BindView(R.id.sync_notify)
    View mSyncNotify;

    @BindView(R.id.sync_start)
    View mSyncStartButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSyncStartButton.setOnClickListener(v -> mPresenter.startSynchronization());
    }

    @Override
    public void notifyConnection(boolean isConnected) {
        mConnectionView.setVisibility(isConnected ? GONE : VISIBLE);
    }

    @Override
    public void notifySynchronized(boolean isSynchronized) {
        mSyncNotify.setVisibility(isSynchronized ? GONE : VISIBLE);
    }
}