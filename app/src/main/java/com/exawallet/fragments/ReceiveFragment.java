package com.exawallet.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import com.devspark.robototextview.widget.RobotoTextView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.ReceivePresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.WalletManageToolbar;
import com.exawallet.views.IReceiveView;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.RECEIVE;

public class ReceiveFragment extends BaseSyncWalletFragment<ReceivePresenter, IReceiveView, BaseHomeToolbar> implements IReceiveView {
    @BindView(R.id.address)
    RobotoTextView mAddress;

    @BindView(R.id.copy_button)
    View mCopyButton;

    @BindView(R.id.share_button)
    View mReceiveButton;

    @BindView(R.id.qr_code)
    View mQRCode;

    @BindView(R.id.payment_id_layout)
    TextInputLayout mPaimentIdLayout;

    @BindView(R.id.payment_id)
    EditText mPaimentIdEditText;

    @BindView(R.id.payment_id_button)
    View mPaimentIdButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCopyButton.setOnClickListener(v -> mPresenter.onCopyButtoon());
        mAddress.setOnClickListener(v -> mPresenter.onCopyButtoon());
        mReceiveButton.setOnClickListener(v -> mPresenter.onShareButton());

        mPaimentIdEditText.addTextChangedListener(mPresenter.getPaimentIdWatcher());
        mPaimentIdButton.setOnClickListener(v -> mPresenter.onPaimentId(mPaimentIdEditText));

        BACK_PATH.setScreen(RECEIVE);
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new WalletManageToolbar(view, R.string.action_receive, (RootActivity) getActivity()) {

            @Override
            protected void onButton()  {
                ((RootActivity) getActivity()).show(WalletManageFragment.newInstance());
            }
        };
    }

    @Override
    int getLayout() {
        return R.layout.fragment_receive;
    }

    @Override
    ReceivePresenter createPresenter() {
        return new ReceivePresenter();
    }

    @Override
    public void setAddress(String address) {
        mAddress.setText(address);
    }

    @Override
    public void showQRImage(Bitmap bitmap) {
        if (null != mQRCode) {
            mQRCode.setBackground(new BitmapDrawable(getResources(), bitmap));
        }
    }

    @Override
    public void setPaymentIdError(String message) {
        setLayoutError(message, mPaimentIdLayout);
    }

    private void setLayoutError(String message, TextInputLayout textInputLayout) {
        textInputLayout.setError(message);
        textInputLayout.setErrorEnabled(!isEmpty(message));
    }

    public static ReceiveFragment newInstance() {
        return new ReceiveFragment();
    }
}