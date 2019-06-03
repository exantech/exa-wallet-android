package com.exawallet.toolbars;

import android.view.View;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;

public abstract class BaseSettingsToolbar extends BaseToolbar {
    @BindView(R.id.toolbar_button)
    View mButton;

    protected BaseSettingsToolbar(View view, int titleResId, RootActivity activity) {
        super(view, titleResId, activity);

        mButton.setBackgroundResource(getButtonIcon());
        mButton.setOnClickListener(v -> onButton());
    }

    protected abstract void onButton();

    private int getButtonIcon() {
        return R.drawable.ic_settings_white;
    }
}
