package com.exawallet.views;

public interface IBaseView {
    void showInfoDialog(String message);

    void showErrorDialog(String title, String message);

    void showErrorDialog(String title, int buttonOk, String message, Runnable runnable);
}
