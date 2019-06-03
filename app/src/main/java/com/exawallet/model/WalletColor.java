package com.exawallet.model;


import com.exawallet.monerowallet.R;

public enum WalletColor {
    ORANGE(R.color.orange),
    TANGELO(R.color.tangelo),
    BLUE(R.color.blue),
    PURPLE(R.color.purple),
    GREEN(R.color.green),
    PALE(R.color.pale);

    private final int mValue;

    WalletColor(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
