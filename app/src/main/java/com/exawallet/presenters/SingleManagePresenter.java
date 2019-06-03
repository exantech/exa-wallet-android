package com.exawallet.presenters;

import com.exawallet.dialogs.TwoButtonDialog;
import com.exawallet.fragments.ChangePasswordFragment;
import com.exawallet.fragments.EditWalletFragment;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IBaseView;

import java.io.IOException;

import static com.exawallet.presenters.operations.LoadContentOperation.loadContent;
import static com.exawallet.utils.BackPath.BACK_PATH;

public class SingleManagePresenter extends AttachedPresenter<IBaseView> {
    public void onEditWallet() {
        mActivity.show(EditWalletFragment.newInstance());
    }

    public void onChangePassword() {
        mActivity.show(ChangePasswordFragment.newInstance());
    }

    public void onDeleteWallet() {
        new TwoButtonDialog(mActivity, getString(R.string.warning), getString(R.string.delete_wallet_notify)) {
            @Override
            protected void onButtonOkClick() {
                try {
                    mWalletMeta.deleted();
                    BACK_PATH.clear();
                    dismiss();
                    loadContent(mActivity);
                } catch (IOException e) {
                    mView.showErrorDialog(getString(R.string.error), e.getLocalizedMessage());
                }
            }
        }.show();
    }
}