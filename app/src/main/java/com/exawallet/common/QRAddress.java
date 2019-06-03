package com.exawallet.common;

import android.graphics.Bitmap;

public class QRAddress implements IEvent {
    private final Bitmap mBitmap;

    public QRAddress(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}