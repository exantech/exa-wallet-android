package com.exawallet.dialogs;

import android.content.Context;
import android.os.Bundle;
import com.exawallet.monerowallet.R;

public abstract class TwoButtonDialog extends CustomDialog {
    protected TwoButtonDialog(Context context, String title, String content) {
        super(context, title, content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.button_ok).setOnClickListener(view -> onButtonOkClick());
    }

    protected abstract void onButtonOkClick();

    @Override
    protected int getIcon() {
        return R.drawable.ic_info_outline_blue;
    }

    @Override
    int getLayout() {
        return R.layout.dialog_two_button;
    }
}
