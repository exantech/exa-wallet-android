package com.exawallet.presenters.operations;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.listeners.OnQRGenerateListener;
import com.exawallet.presenters.results.QRCodeResult;
import com.exawallet.utils.HLog;
import com.google.zxing.WriterException;

import static com.exawallet.engine.Engine.ENGINE;

public class QRCodeOperation extends NoSplashScreenOperation<QRCodeResult> {
    private final String mMessage;
    private final OnQRGenerateListener mListener;

    private QRCodeOperation(RootActivity activity, String message, OnQRGenerateListener listener) {
        super(activity);
        this.mMessage = message;
        this.mListener = listener;
    }

    @Override
    protected QRCodeResult execute() {
        try {
            return new QRCodeResult(new QRGEncoder(mMessage, null, QRGContents.Type.TEXT, (int) mActivity.getResources().getDimension(R.dimen.size560)).encodeAsBitmap());
        } catch (WriterException e) {
            return new QRCodeResult(e);
        }
    }

    @Override
    protected void onSuccess(QRCodeResult result) {
        HLog.debug(TAG, "onSuccess");
        mListener.onGenerate(result);
    }

    public static void generateQRCode(RootActivity activity, String message, OnQRGenerateListener listener) {
        ENGINE.submit(new QRCodeOperation(activity, message, listener));
    }
}