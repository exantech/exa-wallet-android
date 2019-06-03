package com.exawallet.presenters;

import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import com.exawallet.dialogs.TwoButtonDialog;
import com.exawallet.fragments.RestoreWalletFragment;
import com.exawallet.model.RestoreMnemonicState;
import com.exawallet.monerowallet.R;
import com.exawallet.utils.SimpleTextWatcher;
import com.exawallet.views.IMnemonicView;

import java.util.Date;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.model.ElectrumUtils.validateMnemonic;
import static com.exawallet.model.RestoreMnemonicState.SUCCESS;
import static com.exawallet.utils.Screens.*;
import static com.exawallet.utils.Utils.*;
import static java.lang.String.valueOf;

public class RestoreMnemonicPresenter extends BasePresenter<IMnemonicView> {
    private String mMnemonicText;
    private String mLanguage;
    private long mBlockHeight;
    private String mBlockOrDate;

    public void onRestore() {
        if (!isEmpty(mMnemonicText) && -1 < mBlockHeight) {
            if (0 == mBlockHeight) {
                new TwoButtonDialog(mActivity, getString(R.string.skip_blockheight), getString(R.string.skip_blockheight_notify)) {
                    @Override
                    protected void onButtonOkClick() {
                        restoreWallet();
                        dismiss();
                    }

                    @Override
                    protected void onButtonClick() {
                        super.onButtonClick();
                        mView.enableContinueButton(true);
                    }
                }.show();
            } else {
                restoreWallet();
            }
        } else {
            mView.showErrorDialog(getString(R.string.error), getString(R.string.error));
        }
    }

    private void restoreWallet() {
        RESTORE_MNEMONIC.pushObject(MNEMONIC, mMnemonicText);
        RESTORE_MNEMONIC.pushObject(BLOCK_OR_DATE, mBlockOrDate);
        mActivity.show(RestoreWalletFragment.newInstance(mMnemonicText, mLanguage, mBlockHeight));
    }

    public TextWatcher onBlockOrDate() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mBlockOrDate = valueOf(editable).toLowerCase();

                if (!isEmpty(mBlockOrDate)) {
                    try {
                        mBlockHeight = Long.parseLong(mBlockOrDate);
                        mView.showBlockHeightError(-1);
                    } catch (Exception e) {
                        try {
                            mBlockHeight = getBlockHeightByDate(APP_CONTEXT.getNetworkType(), DATE_SHORT_FORMAT.parse(mBlockOrDate).getTime() / 1000);
                            mView.showBlockHeightError(-1);
                        } catch (Exception t) {
                            mBlockHeight = -1L;
                            mView.showBlockHeightError(R.string.invalid_block_height);
                        }
                    }
                } else {
                    mBlockHeight = 0L;
                }
            }
        };
    }

    public TextWatcher onMnemonic() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable mnemonic) {
                mMnemonicText = valueOf(mnemonic).toLowerCase();

                boolean fixed = false;

                while (mMnemonicText.contains("  ") || mMnemonicText.contains("\n")) {
                    mMnemonicText = mMnemonicText.replaceAll("  ", " ").replaceAll("\n", " ").replaceAll("\\s\\s+", " ").trim();
                    fixed = true;
                }

                if (fixed) {
                    mView.showMnemonic(mMnemonicText);
                } else {
                    RestoreMnemonicState restoreMnemonicState = validateMnemonic(mMnemonicText);

                    if (SUCCESS == restoreMnemonicState) {
                        mLanguage = restoreMnemonicState.getLanguage();
                    }

                    mView.showMnemonicError(SUCCESS == restoreMnemonicState ? -1 : restoreMnemonicState.getMessage());
                    mView.enableContinueButton(SUCCESS == restoreMnemonicState && -1 < mBlockHeight);
                }
            }
        };
    }


    public void onCalendar() {
        Date time = new Date();
        new DatePickerDialog(mActivity, (view, year, month, dayOfMonth) -> mView.setDate(formatDateShort(new Date(year - 1900, month, dayOfMonth))), 1900 + time.getYear(), time.getMonth(), time.getDate()).show();
    }
}