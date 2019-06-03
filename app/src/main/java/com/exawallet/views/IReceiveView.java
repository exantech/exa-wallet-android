package com.exawallet.views;

import android.graphics.Bitmap;

public interface IReceiveView extends ISynchronizationView {
    void setAddress(String address);

    void showQRImage(Bitmap bitmap);

    void setPaymentIdError(String message);
}
