package com.exawallet.presenters;

import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import com.exawallet.fragments.ConfirmMnemonicFragment;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IBaseView;

import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.model.ElectrumUtils.getWordList;
import static com.exawallet.widget.SecretTextView.initShadowCollector;

public class ShowMnemonicPresenter extends AttachedPresenter<IBaseView> {
    public ListAdapter getMnemonicAdapter() {
        initShadowCollector(getWordList(mWalletMeta.getLanguage()), APP_CONTEXT.useSpyProtection());
        return new ArrayAdapter<>(mActivity, R.layout.layout_mnemonic_item_white, R.id.mnemonic_item, mWallet.getSeed().split(" "));
    }

    public void onContinueButton() {
        mActivity.show(ConfirmMnemonicFragment.newInstance());
    }
}