package com.exawallet.toolbars;

import android.view.View;
import com.exawallet.RootActivity;

abstract class BaseNavigationToolbar extends BaseToolbar {
    BaseNavigationToolbar(View view, int titleResId, RootActivity activity) {
        super(view, titleResId, activity);

        mToolbar.setNavigationIcon(getNavigationIcon());
        mToolbar.setNavigationOnClickListener(v -> onNavigationClick());
    }

    protected abstract void onNavigationClick();

    protected abstract int getNavigationIcon();
}