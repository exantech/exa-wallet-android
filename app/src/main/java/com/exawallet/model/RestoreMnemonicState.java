package com.exawallet.model;

import com.exawallet.monerowallet.R;

public enum RestoreMnemonicState {
    INVALID_MNEMONIC_LENGTH(R.string.invalid_mnemonic_length),
    UNKNOW_LANGUAGE(R.string.unknow_mnemonic_language),
    SUCCESS(R.string.success);

    private final int mMessage;
    private String language;

    RestoreMnemonicState(int message) {
        mMessage = message;
    }

    public int getMessage() {
        return mMessage;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
