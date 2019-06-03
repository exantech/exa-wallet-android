package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.RestoreMnemonicPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.IMnemonicView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.*;

public class RestoreMnemonicFragment extends BaseToolbarFragment<RestoreMnemonicPresenter, IMnemonicView, BaseHomeToolbar> implements IMnemonicView {
    @BindView(R.id.block_or_date)
    EditText mBlockOrDate;

    @BindView(R.id.mnemonic)
    EditText mMnemonic;

    @BindView(R.id.calendar)
    View mCalendar;

    @BindView(R.id.continue_button)
    View mContinueButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBlockOrDate.addTextChangedListener(mPresenter.onBlockOrDate());
        mBlockOrDate.setText((CharSequence) RESTORE_MNEMONIC.popObject(BLOCK_OR_DATE));

        mMnemonic.addTextChangedListener(mPresenter.onMnemonic());
        mMnemonic.setText((CharSequence) RESTORE_MNEMONIC.popObject(MNEMONIC));

        mCalendar.setOnClickListener(v -> mPresenter.onCalendar());

        setOnClickListener(mContinueButton, () -> mPresenter.onRestore());

        BACK_PATH.setScreen(RESTORE_MNEMONIC);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_restore_mnemonic;
    }

    @Override
    RestoreMnemonicPresenter createPresenter() {
        return new RestoreMnemonicPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_restore_wallet, (RootActivity) getActivity());
    }

    @Override
    public void enableContinueButton(boolean enable) {
        mContinueButton.setEnabled(enable);
    }

    @Override
    public void setDate(String date) {
        mBlockOrDate.setText(date);
    }

    @Override
    public void showMnemonic(String mnemonicText) {
        mMnemonic.setText(mnemonicText);
        mMnemonic.setSelection(mnemonicText.length());
    }

    @Override
    public void showMnemonicError(int mnemonicError) {
        mMnemonic.setError(-1 != mnemonicError ? getString(mnemonicError) : null);
    }

    @Override
    public void showBlockHeightError(int blockHeightError) {
        mBlockOrDate.setError(-1 != blockHeightError ? getString(blockHeightError) : null);
    }

    public static BaseFragment newInstance() {
        return new RestoreMnemonicFragment();
    }
}