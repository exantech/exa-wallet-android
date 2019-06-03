package com.exawallet.views;

public interface IPinView extends IBaseView {
    void setPin0Background(int resourceId);

    void setPin1Background(int resourceId);

    void setPin2Background(int resourceId);

    void setPin3Background(int resourceId);

    void showWaitTitle(long pinTime);

    void showDefaultTitle();

    void setExitButtonVisible(boolean visible);
}