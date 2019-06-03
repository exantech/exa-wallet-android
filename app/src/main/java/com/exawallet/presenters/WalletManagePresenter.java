package com.exawallet.presenters;

import android.widget.TextView;
import com.exawallet.dialogs.TwoButtonDialog;
import com.exawallet.fragments.ChangePasswordFragment;
import com.exawallet.fragments.EditWalletFragment;
import com.exawallet.fragments.RemindMnemonicFragment;
import com.exawallet.fragments.SyncSplashFragment;
import com.exawallet.model.meta.SharedMeta;
import com.exawallet.monerowallet.BuildConfig;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IWalletManageView;

import java.io.IOException;

import static com.exawallet.model.meta.SharedMeta.buildEmpty;
import static com.exawallet.presenters.operations.LoadContentOperation.loadContent;
import static com.exawallet.utils.BackPath.BACK_PATH;

public class WalletManagePresenter extends BaseSyncWalletPresenter<IWalletManageView> {
    @Override
    public void onResume() {
        super.onResume();
        SharedMeta sharedMeta = mWalletMeta.getSharedMeta();
        mView.setMnemonic(null == sharedMeta || sharedMeta.hasSeed());
        mView.setChangeMode(BuildConfig.DEBUG, mWalletMeta.isSharedReady());
    }

    public void onEditWallet() {
        mActivity.show(EditWalletFragment.newInstance());
    }

    public void onChangePassword() {
        mActivity.show(ChangePasswordFragment.newInstance());
    }

    public void onRescan() {
        mWallet.rescanBlockchainAsync();
        mActivity.show(SyncSplashFragment.newInstance());
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

    public void onRemindMnemonic() {
        mActivity.show(RemindMnemonicFragment.newInstance());
    }

    public void onChangeWalletType(TextView changeWalletType) {
        mWalletMeta.setSharedMeta(mWalletMeta.isSharedReady() ? null : buildEmpty());
        changeWalletType.setText(mWalletMeta.isSharedReady() ? R.string.action_set_personal : R.string.action_set_shared);
    }

    public void onDropImported() {
        if (null != mWalletMeta.getSharedMeta()) {
            mWalletMeta.getSharedMeta().setImported(null);
        }
    }
}