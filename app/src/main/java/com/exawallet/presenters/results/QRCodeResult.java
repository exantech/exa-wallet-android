package com.exawallet.presenters.results;

import android.graphics.Bitmap;
import com.exawallet.engine.results.ExceptionResult;

public class QRCodeResult extends ExceptionResult<Bitmap> {
    public QRCodeResult(Bitmap result) {
        super(result);
    }

    public QRCodeResult(Exception exception) {
        super(exception);
    }
}