package com.exawallet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.exawallet.RootActivity;
import com.exawallet.dialogs.ErrorDialog;
import com.exawallet.dialogs.ErrorTwoButtonDialog;
import com.exawallet.dialogs.UpDialog;
import com.exawallet.presenters.BasePresenter;
import com.exawallet.views.IBaseView;

import static com.exawallet.common.Analytics.ANALYTICS;

public abstract class BaseFragment<P extends BasePresenter, V extends IBaseView> extends Fragment {
    static String TAG = "BaseFragment";
    private Unbinder mUnbinder;
    P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TAG = this.getClass().getSimpleName();

        mPresenter = createPresenter();
        mPresenter.onAttach((RootActivity) getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.onCreate((V) this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(getLayout(), null);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        ANALYTICS.logScreen(TAG, "onResume");
        mPresenter.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        ANALYTICS.logScreen(TAG, "onStop");
        mPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.onDestroy();
        if (null != mUnbinder) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mPresenter.onDetach();
    }

    public void showInfoDialog(String message) {
        new UpDialog(getActivity(), message).show();
    }

    public void showErrorDialog(String title, String message) {
        new ErrorDialog(getActivity(), title, message).show();
    }

    public void showErrorDialog(String title, int buttonOk, String message, Runnable runnable) {
        new ErrorTwoButtonDialog(getActivity(), title, message, getString(buttonOk)) {
            @Override
            protected void onButtonOkClick() {
                runnable.run();
                dismiss();
            }
        }.show();
    }

    abstract int getLayout();

    abstract P createPresenter();

    static void setOnClickListener(View button, Runnable runnable) {
        button.setOnClickListener(v -> {
            button.setEnabled(false);
            runnable.run();
        });
    }
}
