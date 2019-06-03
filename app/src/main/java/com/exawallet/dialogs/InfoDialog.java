package com.exawallet.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import com.exawallet.monerowallet.R;

public class InfoDialog extends CustomDialog {

    private final String mSubTitle;

    public InfoDialog(Context context, String title, String subTitle, String content) {
        super(context, title, content);
        this.mSubTitle = subTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView subtitle = findViewById(R.id.subtitle);
        subtitle.setText(mSubTitle);
    }

    @Override
    protected int getIcon() {
        return R.drawable.ic_tick;
    }

    @Override
    int getLayout() {
        return R.layout.dialog_info;
    }
}
