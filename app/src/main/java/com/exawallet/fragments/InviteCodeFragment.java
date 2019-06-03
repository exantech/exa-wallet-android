package com.exawallet.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.devspark.robototextview.widget.RobotoTextView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.InviteCodePresenter;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.toolbars.WalletManageToolbar;
import com.exawallet.views.IInviteCodeView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SELECT_WALLET_SCREEN;

public class InviteCodeFragment extends BaseToolbarFragment<InviteCodePresenter, IInviteCodeView, BaseToolbar> implements IInviteCodeView {
    @BindView(R.id.participants_progress)
    TextView mParticipantsProgress;

    @BindView(R.id.invite_code)
    RobotoTextView mInviteCode;

    @BindView(R.id.copy_button)
    View mCopyButton;

    @BindView(R.id.share_button)
    View mShareButton;

    @BindView(R.id.qr_code)
    View mQRCode;

    @Override
    int getLayout() {
        return R.layout.fragment_remind_invite_code;
    }

    @Override
    InviteCodePresenter createPresenter() {
        return new InviteCodePresenter();
    }

    @Override
    BaseToolbar createToolbar(View view) {
        return new WalletManageToolbar(view, R.string.action_remind_invite_code, (RootActivity) getActivity()) {

            @Override
            protected void onButton() {
                ((RootActivity) getActivity()).show(SingleManageFragment.newInstance());
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCopyButton.setOnClickListener(v -> mPresenter.onCopyButtoon());
        mInviteCode.setOnClickListener(v -> mPresenter.onCopyButtoon());
        mShareButton.setOnClickListener(v -> mPresenter.onShareButton());

        BACK_PATH.setScreen(SELECT_WALLET_SCREEN);
    }

    @Override
    public void setParticipantsProgress(int progress, int count) {
        mParticipantsProgress.setText(String.format(getString(R.string.participants_progress), progress, count));
    }

    @Override
    public void setInviteCode(String inviteCode) {
        mInviteCode.setText(inviteCode);
    }

    @Override
    public void showQRImage(Bitmap bitmap) {
        mQRCode.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    public static BaseFragment newInstance() {
        return new InviteCodeFragment();
    }
}