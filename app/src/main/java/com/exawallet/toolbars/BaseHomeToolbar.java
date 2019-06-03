package com.exawallet.toolbars;

import android.view.View;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;

public class BaseHomeToolbar extends BaseNavigationToolbar {
    boolean isClicked = false;

    public BaseHomeToolbar(View view, int titleResId, RootActivity activity) {
        super(view, titleResId, activity);
    }

    protected int getNavigationIcon() {
        return R.drawable.ic_arrow_back_white;
    }

    @Override
    protected void onNavigationClick() {
        if (!isClicked) {
            isClicked = true;
            mActivity.goBack();
        }
    }
}