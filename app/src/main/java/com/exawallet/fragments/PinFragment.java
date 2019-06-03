package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.PinPresenter;
import com.exawallet.utils.HLog;
import com.exawallet.views.IPinView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class PinFragment<P extends PinPresenter, V extends IPinView> extends BaseFragment<P, V> implements IPinView {
    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.pin0)
    View mPin0;

    @BindView(R.id.pin1)
    View mPin1;

    @BindView(R.id.pin2)
    View mPin2;

    @BindView(R.id.pin3)
    View mPin3;

    @BindView(R.id.button_0)
    View mButton0;

    @BindView(R.id.button_1)
    View mButton1;

    @BindView(R.id.button_2)
    View mButton2;

    @BindView(R.id.button_3)
    View mButton3;

    @BindView(R.id.button_4)
    View mButton4;

    @BindView(R.id.button_5)
    View mButton5;

    @BindView(R.id.button_6)
    View mButton6;

    @BindView(R.id.button_7)
    View mButton7;

    @BindView(R.id.button_8)
    View mButton8;

    @BindView(R.id.button_9)
    View mButton9;

    @BindView(R.id.button_back)
    View mButtonBack;

    @BindView(R.id.button_exit)
    View mButtonExit;

    @BindView(R.id.exit_title_text_view)
    TextView mExitTitleTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton0.setOnClickListener(view1 -> mPresenter.onButton0Click());
        mButton1.setOnClickListener(view1 -> mPresenter.onButton1Click());
        mButton2.setOnClickListener(view1 -> mPresenter.onButton2Click());
        mButton3.setOnClickListener(view1 -> mPresenter.onButton3Click());
        mButton4.setOnClickListener(view1 -> mPresenter.onButton4Click());
        mButton5.setOnClickListener(view1 -> mPresenter.onButton5Click());
        mButton6.setOnClickListener(view1 -> mPresenter.onButton6Click());
        mButton7.setOnClickListener(view1 -> mPresenter.onButton7Click());
        mButton8.setOnClickListener(view1 -> mPresenter.onButton8Click());
        mButton9.setOnClickListener(view1 -> mPresenter.onButton9Click());
        mButtonBack.setOnClickListener(view1 -> mPresenter.onButtonBackClick());
        mButtonExit.setOnClickListener(view1 -> mPresenter.onButtonSkipClick());

        showDefaultTitle();
    }

    public void setPin0Background(int resourceId) {
        mPin0.setBackgroundResource(resourceId);
    }

    public void setPin1Background(int resourceId) {
        mPin1.setBackgroundResource(resourceId);
    }

    public void setPin2Background(int resourceId) {
        mPin2.setBackgroundResource(resourceId);
    }

    public void setPin3Background(int resourceId) {
        mPin3.setBackgroundResource(resourceId);
    }

    @Override
    public void showWaitTitle(long pinTime) {
        mTitle.setText(String.format(getString(R.string.waiting), pinTime / 1000));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_pin;
    }

    @Override
    public void setExitButtonVisible(boolean visible) {
        HLog.debug(TAG, "setExitButtonVisible: visible=" + visible);
        final int visibility = visible ? VISIBLE : GONE;
        mButtonExit.setVisibility(visibility);
        mExitTitleTextView.setVisibility(visibility);
    }
}