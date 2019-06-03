package com.exawallet.common;

import android.graphics.Bitmap;

public class QRInviewCode implements IEvent {
    private final Bitmap mBitmap;

    public QRInviewCode(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}