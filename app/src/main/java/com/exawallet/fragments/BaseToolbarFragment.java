package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.exawallet.presenters.BasePresenter;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IBaseView;

public abstract class BaseToolbarFragment<P extends BasePresenter, V extends IBaseView, T extends BaseToolbar> extends BaseFragment<P, V> {
    T mBaseToolbar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBaseToolbar = createToolbar(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBaseToolbar.onDestroy();
    }

    abstract T createToolbar(View view);
}