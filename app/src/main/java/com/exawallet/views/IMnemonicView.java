package com.exawallet.views;

public interface IMnemonicView extends IBaseView {
    void enableContinueButton(boolean enable);

    void setDate(String string);

    void showMnemonic(String mnemonicText);

    void showMnemonicError(int message);

    void showBlockHeightError(int message);
}
