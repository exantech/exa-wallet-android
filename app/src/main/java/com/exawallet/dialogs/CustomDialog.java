package com.exawallet.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;
import android.widget.TextView;
import com.exawallet.monerowallet.R;

abstract class CustomDialog extends AppCompatDialog {
    private final String mTitle;
    private final String mContent;

    CustomDialog(Context context, String title, String content) {
        super(context);
        this.mTitle = title;
        this.mContent = content;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayout());

        findViewById(R.id.icon).setBackgroundResource(getIcon());
        this.<TextView>findViewById(R.id.title).setText(getTitle());
        this.<TextView>findViewById(R.id.content).setText(getContent());

        findViewById(R.id.button).setOnClickListener(view -> onButtonClick());
    }

    protected abstract int getIcon();

    private String getTitle() {
        return mTitle;
    }

    private String getContent() {
        return mContent;
    }

    protected void onButtonClick() {
        dismiss();
    }

    abstract int getLayout();
}
