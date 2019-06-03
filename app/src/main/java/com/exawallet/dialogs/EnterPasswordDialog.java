package com.exawallet.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.listeners.OnPasswordListener;

import static java.lang.String.valueOf;

public class EnterPasswordDialog extends AppCompatDialog {
    private final OnPasswordListener mOnPasswordListener;
    private EditText mPassword;
    private View mHidePassword;

    private boolean isHidePassword;

    public EnterPasswordDialog(Context context, OnPasswordListener onPasswordListener) {
        super(context);
        this.mOnPasswordListener = onPasswordListener;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_enter_password);

        mHidePassword = findViewById(R.id.hide_password);
        mHidePassword.setOnClickListener(view -> {
            mHidePassword.setBackgroundResource(isHidePassword ? R.drawable.ic_visibility_off_gray : R.drawable.ic_visibility_gray);
            mPassword.setTransformationMethod(isHidePassword ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
            mPassword.setSelection(mPassword.getText().length());
            isHidePassword = !isHidePassword;
        });

        mPassword = findViewById(R.id.password);
        View button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            dismiss();
            mOnPasswordListener.onPassword(valueOf(mPassword.getText()));
        });
    }
}
