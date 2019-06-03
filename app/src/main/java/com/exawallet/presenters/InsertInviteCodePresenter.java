package com.exawallet.presenters;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import com.exawallet.fragments.JoinSharedWalletFragment;
import com.exawallet.presenters.listeners.OnQRScanListener;
import com.exawallet.utils.SimpleTextWatcher;
import com.exawallet.views.IInsertInviteCodeView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.exawallet.utils.Screens.INSERT_INVITE_CODE;
import static com.exawallet.utils.Screens.INVITE_CODE;
import static java.lang.String.valueOf;

public class InsertInviteCodePresenter extends BasePresenter<IInsertInviteCodeView> implements OnQRScanListener {
    private String mInviteCode;

    public TextWatcher onInviteCode() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable inviteCode) {
                mInviteCode = valueOf(inviteCode);
            }
        };
    }

    public void onScan() {
        mActivity.setOnQRScanListener(this);
        new IntentIntegrator(mActivity).initiateScan();
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        mActivity.setOnQRScanListener(null);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (null != result && null != result.getContents()) {
            mView.setInviteCode(result.getContents());
            return false;
        } else {
            return true;
        }
    }

    public void onContinue() {
        INSERT_INVITE_CODE.pushObject(INVITE_CODE, mInviteCode);
        mActivity.show(JoinSharedWalletFragment.newInstance(mInviteCode));
    }
}