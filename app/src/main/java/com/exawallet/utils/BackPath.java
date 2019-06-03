package com.exawallet.utils;

import java.util.ArrayList;
import java.util.List;

import static com.exawallet.utils.Screens.MESSAGE;
import static com.exawallet.utils.Screens.SPLASH;

public enum BackPath {
    BACK_PATH;

    private Screens mCurrent;

    private final List<Screens> mScreens = new ArrayList<>();

    public boolean isEmpty() {
        return 1 > mScreens.size();
    }

    public Screens getScreen() {
        int index = mScreens.size() - 1;

        if (-1 < index) {
            Screens result = mScreens.get(index);
            mScreens.remove(index);

            clearScreen(mCurrent);
            mCurrent = result;
            return result;
        }

        return null;
    }

    public boolean canBack(){
        return 0 < mScreens.size();
    }

    public Screens getCurrent() {
        return mCurrent;
    }

    public String getMessage() {
        return null != mCurrent ? (String) mCurrent.popObject(MESSAGE) : null;
    }

    public void setScreen(Screens screen) {
        if (screen != mCurrent) {
            if (null != mCurrent && SPLASH != mCurrent) {
                mScreens.add(mCurrent);
            }
            mCurrent = screen;
        }
    }

    public void clear() {
        clearScreen(mCurrent);
        for (Screens screen: mScreens) {
            clearScreen(screen);
        }

        mCurrent = null;
        mScreens.clear();
    }

    private void clearScreen(Screens screens) {
        if (null != screens) {
            screens.clear();
        }
    }
}