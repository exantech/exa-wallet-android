package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.CreateSharedWalletPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.views.ICreateSharedWalletView;

import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.CREATE_SHARED_WALLET;

public class CreateSharedWalletFragment extends CreateWalletFragment<CreateSharedWalletPresenter> implements ICreateSharedWalletView {
    @BindView(R.id.signers)
    Spinner mSignersSpinner;

    @BindView(R.id.participants)
    Spinner mParticipantsSpinner;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSignersSpinner.setAdapter(new ArrayAdapter(getContext(), R.layout.spinner_item, getResources().getStringArray(R.array.signers)));
        mSignersSpinner.setSelection(0);
        mSignersSpinner.setOnItemSelectedListener(mPresenter.OnSelectSignatures());

        mParticipantsSpinner.setAdapter(new ArrayAdapter(getContext(), R.layout.spinner_item, getResources().getStringArray(R.array.participants)));
        mParticipantsSpinner.setSelection(0);
        mParticipantsSpinner.setOnItemSelectedListener(mPresenter.OnSelectMembers());

        BACK_PATH.setScreen(CREATE_SHARED_WALLET);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_create_multisig_wallet;
    }

    @Override
    CreateSharedWalletPresenter createPresenter() {
        return new CreateSharedWalletPresenter();
    }

    @Override
    BaseHomeToolbar createToolbar(View view) {
        return new BaseHomeToolbar(view, R.string.action_create_shared_wallet, (RootActivity) getActivity());
    }

    public static BaseFragment newInstance() {
        return new CreateSharedWalletFragment();
    }

    @Override
    public void fixParticipants(int participants) {
        mParticipantsSpinner.setSelection(participants);
    }

    @Override
    public void fixSigners(int signers) {
        mSignersSpinner.setSelection(signers);
    }
}