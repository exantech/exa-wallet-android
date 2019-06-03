package com.exawallet.presenters;

import android.view.View;
import android.widget.AdapterView;
import com.exawallet.dialogs.TwoButtonDialog;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IRecreateWalletView;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.ElectrumUtils.getMnemonicLanguage;

public abstract class CreateWalletPresenter<V extends IRecreateWalletView> extends RecreateWalletPresenter<V> {
    String mLanguage;

    public AdapterView.OnItemSelectedListener OnSelectLanguage() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLanguage = getMnemonicLanguage(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mLanguage = getMnemonicLanguage(0);
            }
        };
    }

    public void onCreateWallet(String password, String walletName, String node) {
        if (isEmpty(password)) {
            new TwoButtonDialog(mActivity, getString(R.string.action_skip_wallet_password), getString(R.string.skip_wallet_password_notify)) {
                @Override
                protected void onButtonOkClick() {
                    buildWallet(password, walletName, node);
                    dismiss();
                }

                @Override
                protected void onButtonClick() {
                    super.onButtonClick();
                    mView.checkButton();
                }
            }.show();
        } else {
            buildWallet(password, walletName, node);
        }
    }

    protected abstract void buildWallet(String password, String walletName, String node);
}