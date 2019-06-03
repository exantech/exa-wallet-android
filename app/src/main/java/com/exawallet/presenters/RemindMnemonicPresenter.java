package com.exawallet.presenters;

import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import com.exawallet.monerowallet.R;
import com.exawallet.utils.HLog;
import com.exawallet.views.IBaseView;

import java.security.NoSuchAlgorithmException;

import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.model.ElectrumUtils.getWordList;
import static com.exawallet.widget.SecretTextView.initShadowCollector;

public class RemindMnemonicPresenter extends AttachedPresenter<IBaseView> {
    public ListAdapter getMnemonicAdapter() {
        try {
            initShadowCollector(getWordList(mWalletMeta.getLanguage()), APP_CONTEXT.useSpyProtection());
            return new ArrayAdapter<>(mActivity, R.layout.layout_mnemonic_item_black, R.id.mnemonic_item, (null != mWalletMeta.getSharedMeta() ? mWalletMeta.getSharedMeta().getSeed(mWallet.getSecretSpendKey()) : mWallet.getSeed()).split(" "));
        } catch (NoSuchAlgorithmException e) {
            HLog.error(TAG, "Can't read seed", e);

            mView.showErrorDialog("Can't read seed", e.getMessage());
        }

        return null;
    }
}