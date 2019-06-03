package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.ChangePasswordPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IChangePasswordView;
import com.exawallet.views.IRecreateWalletView;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.WalletManager.MIN_PASSWORD_LENGTH;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.CHANGE_PASSWORD;
import static java.lang.String.valueOf;

public class ChangePasswordFragment extends BaseToolbarFragment<ChangePasswordPresenter, IRecreateWalletView, BaseToolbar> implements IChangePasswordView {
    @BindView(R.id.password_layout)
    TextInputLayout mPasswordLayout;

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.confirm)
    EditText mConfirm;

    @BindView(R.id.security)
    View mSecurity;

    @BindView(R.id.continue_button)
    View mContinueButton;

    private float mWidth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth() - 2 * getResources().getDimension(R.dimen.size63);

        mPassword.addTextChangedListener(mPresenter.getPasswordWatcher());
        mConfirm.addTextChangedListener(mPresenter.getConfirmWatcher());


        setOnClickListener(mContinueButton, () -> {
            Editable text = mPassword.getText();
            if (!isEmpty(text)) {
                mPresenter.onChangePassword(valueOf(text));
            }
        });

        BACK_PATH.setScreen(CHANGE_PASSWORD);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_change_password;
    }

    @Override
    ChangePasswordPresenter createPresenter() {
        return new ChangePasswordPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_change_password, (RootActivity) getActivity());
    }

    public void setSecurity(double security) {
        if (0.01 > security) {
            mSecurity.setBackgroundResource(R.color.shadow);
            mContinueButton.setEnabled(false);
        } else if (0.4 > security) {
            mSecurity.setBackgroundResource(R.color.red);
            mContinueButton.setEnabled(false);
        } else if (0.7 > security) {
            mSecurity.setBackgroundResource(R.color.yellow);
            checkButton();
        } else {
            mSecurity.setBackgroundResource(R.color.green);
            checkButton();
        }

        ViewGroup.LayoutParams layoutParams = mSecurity.getLayoutParams();
        layoutParams.width = (int) (mWidth * security);
        mSecurity.setLayoutParams(layoutParams);
    }

    public void checkButton() {
        Editable password = mPassword.getText();
        Editable confirm = mConfirm.getText();
        mContinueButton.setEnabled(!isEmpty(password) && MIN_PASSWORD_LENGTH < valueOf(password).length() && valueOf(password).equals(valueOf(confirm)));
    }

    public static BaseFragment newInstance() {
        return new ChangePasswordFragment();
    }
}