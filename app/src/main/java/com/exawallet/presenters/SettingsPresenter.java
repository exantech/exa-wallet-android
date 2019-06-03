package com.exawallet.presenters;

import android.widget.CheckBox;
import com.exawallet.fragments.AboutFragment;
import com.exawallet.fragments.SetupPinFragment;
import com.exawallet.views.IBaseView;

import static com.exawallet.common.AppContext.APP_CONTEXT;

public class SettingsPresenter extends BasePresenter<IBaseView> {
    public void onSetupPin() {
        mActivity.show(SetupPinFragment.newInstance());
    }

    public void onAbout() {
        mActivity.show(AboutFragment.newInstance());
    }

    public void onSpyProtection(CheckBox spyProtection) {
        APP_CONTEXT.setUseSpyProtect(spyProtection.isChecked());
    }
}
