package com.exawallet.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;
import android.widget.TextView;
import com.exawallet.monerowallet.R;

public class UpDialog extends AppCompatDialog {
    private final String mContent;

    public UpDialog(Context context, String content) {
        super(context);
        this.mContent = content;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayout());

        findViewById(R.id.icon).setBackgroundResource(getIcon());
        this.<TextView>findViewById(R.id.content).setText(getContent());

        findViewById(R.id.button).setOnClickListener(view -> onButtonClick());
    }

    private String getContent() {
        return mContent;
    }

    private void onButtonClick() {
        dismiss();
    }

    int getIcon() {
        return R.drawable.ic_tick;
    }

    private int getLayout() {
        return R.layout.dialog_up;
    }
}