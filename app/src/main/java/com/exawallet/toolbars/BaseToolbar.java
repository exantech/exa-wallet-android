package com.exawallet.toolbars;

import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;

public class BaseToolbar {
    final RootActivity mActivity;
    private final Unbinder mUnbinder;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public BaseToolbar(View view, int titleResId, RootActivity activity) {
        mActivity = activity;
        mUnbinder = ButterKnife.bind(this, view);
        mToolbar.setTitle(titleResId);
    }

    public void onDestroy() {
        mUnbinder.unbind();
    }

    public void setTitle(int titleResId) {
        this.mToolbar.setTitle(titleResId);
    }
}