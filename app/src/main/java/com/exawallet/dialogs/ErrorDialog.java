package com.exawallet.dialogs;

import android.content.Context;
import com.exawallet.monerowallet.R;

public class ErrorDialog extends CustomDialog {
    private static boolean onTheScreen = false;

    public ErrorDialog(Context context, String title, String content) {
        super(context, title, content);
    }

    @Override
    protected int getIcon() {
        return R.drawable.ic_declined_gray;
    }

    @Override
    int getLayout() {
        return R.layout.dialog_error;
    }

    @Override
    public void show() {
        if (!onTheScreen) {
            onTheScreen = true;
            super.show();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onTheScreen = false;
    }
}
