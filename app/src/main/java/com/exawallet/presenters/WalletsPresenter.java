package com.exawallet.presenters;

import com.exawallet.fragments.AccessWalletFragment;
import com.exawallet.views.IBaseView;

public class WalletsPresenter extends BasePresenter<IBaseView> {
    public void onAdd() {
        mActivity.show(AccessWalletFragment.newInstance());
    }
}