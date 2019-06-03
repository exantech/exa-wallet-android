package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.InsertInviteCodePresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IInsertInviteCodeView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.INSERT_INVITE_CODE;
import static com.exawallet.utils.Screens.INVITE_CODE;

public class InsertInviteCodeFragment extends BaseToolbarFragment<InsertInviteCodePresenter, IInsertInviteCodeView, BaseToolbar> implements IInsertInviteCodeView {
    @BindView(R.id.invite_code)
    EditText mInviteCode;

    @BindView(R.id.scan_button)
    View mScanCode;

    @BindView(R.id.continue_button)
    View mContinueButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInviteCode.addTextChangedListener(mPresenter.onInviteCode());
        mInviteCode.setText((CharSequence) INSERT_INVITE_CODE.popObject(INVITE_CODE));

        mScanCode.setOnClickListener(v -> mPresenter.onScan());

        setOnClickListener(mContinueButton, () -> mPresenter.onContinue());

        BACK_PATH.setScreen(INSERT_INVITE_CODE);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_insert_invite_code;
    }

    @Override
    InsertInviteCodePresenter createPresenter() {
        return new InsertInviteCodePresenter();
    }

    @Override
    BaseToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_insert_invite_code, (RootActivity) getActivity());
    }

    @Override
    public void setInviteCode(String inviteCode) {
        mInviteCode.setText(inviteCode);
    }

    public static BaseFragment newInstance() {
        return new InsertInviteCodeFragment();
    }
}