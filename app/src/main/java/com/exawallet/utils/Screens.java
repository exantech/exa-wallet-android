package com.exawallet.utils;

import java.util.HashMap;

public enum Screens {
    ABOUT,
    ACCESS_WALLET,
    CREATE_PRIVATE_WALLET,
    CREATE_SHARED_WALLET,
    RESTORE_WALLET,
    EDIT_WALLET,
    SINGLE_MANAGE,
    MANAGE_WALLET,
    SEND,
    RECEIVE,
    SHOW_MNEMONIC,
    REMIND_MNEMONIC,
    CHANGE_PASSWORD,
    INSERT_INVITE_CODE,
    JOIN_SHARED_WALLET,

    SELECT_WALLET_SCREEN,

    SUCCESS_MNEMONIC,
    CONFIRM_MNEMONIC,
    RESTORE_MNEMONIC,
    SETTINGS,
    SPLASH,
    WALLETS,
    CHART,
    TRANSACTION_INFO,
    TXPROPOSALS_META;

    public static final String BEHAVIOUR = "behaviour";
    public static final String MESSAGE = "message";
    public static final String MNEMONIC = "mnemonic";
    public static final String INVITE_CODE = "invite_code";
    public static final String BLOCK_OR_DATE = "blockOrDate";

    private HashMap<String, Object> mArguments;

    public Object popObject(String key) {
        return popObject(key, null);
    }

    public Object popObject(String key, Object value) {
        return null == this.mArguments || !mArguments.containsKey(key) ? value : mArguments.remove(key);
    }

    public void pushObject(String key, Object object) {
        if (null == mArguments) {
            mArguments = new HashMap<>();
        }

        mArguments.put(key, object);
    }

    public void clear() {
        mArguments = null;
    }
}