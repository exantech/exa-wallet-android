package com.exawallet.presenters.listeners;

import com.exawallet.presenters.results.QRCodeResult;

public interface OnQRGenerateListener {
    void onGenerate(QRCodeResult result);
}