package com.exawallet.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import com.exawallet.monerowallet.R;

public abstract class ErrorTwoButtonDialog extends CustomDialog {
    private static boolean onTheScreen = false;
    private final String mButtonOk;

    protected ErrorTwoButtonDialog(Context context, String title, String content, String buttonOk) {
        super(context, title, content);
        this.mButtonOk = buttonOk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.button_ok).setOnClickListener(view -> onButtonOkClick());
        ((Button) findViewById(R.id.button_ok)).setText(mButtonOk);
    }

    @Override
    protected int getIcon() {
        return R.drawable.ic_declined_gray;
    }

    protected abstract void onButtonOkClick();

    @Override
    int getLayout() {
        return R.layout.dialog_error_two_button;
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