package com.exawallet.presenters;

import android.text.Editable;
import android.text.TextWatcher;
import com.exawallet.model.WalletColor;
import com.exawallet.utils.SimpleTextWatcher;
import com.exawallet.views.IProcessWalletView;

import java.util.regex.Pattern;

import static com.exawallet.model.WalletColor.*;
import static java.lang.String.valueOf;

public class ProcessWalletPresenter<V extends IProcessWalletView> extends BasePresenter<V> {
    private static final Pattern FILE_MATCH_PATTERN = Pattern.compile("^[a-zA-Z0-9_\\s-]+$");

    WalletColor mWalletColor = ORANGE;

    public TextWatcher getWalletWatcher() {
        return new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mView.checkWalletName(FILE_MATCH_PATTERN.matcher(valueOf(s)).matches());
            }
        };
    }

    public void onOrange() {
        setWalletColor(ORANGE);
    }

    public void onBlue() {
        setWalletColor(BLUE);
    }

    public void onGreen() {
        setWalletColor(GREEN);
    }

    public void onPale() {
        setWalletColor(PALE);
    }

    public void onPurple() {
        setWalletColor(PURPLE);
    }

    public void onTangelo() {
        setWalletColor(TANGELO);
    }

    private void setWalletColor(WalletColor walletColor) {
        mWalletColor = walletColor;
        mView.setWalletColor(mWalletColor);
    }
}
