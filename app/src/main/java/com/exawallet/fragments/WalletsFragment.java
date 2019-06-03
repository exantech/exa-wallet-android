package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.fragments.adapters.WalletsAdapter;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.WalletsPresenter;
import com.exawallet.toolbars.BaseSettingsToolbar;
import com.exawallet.toolbars.BaseToolbar;
import com.exawallet.views.IBaseView;

import java.util.List;

import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.model.NetworkType.NETWORK_TYPE_MAINNET;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.WALLETS;

public class WalletsFragment extends BaseToolbarFragment<WalletsPresenter, IBaseView, BaseToolbar> implements IBaseView {
    @BindView(R.id.add)
    View mAdd;

    @BindView(R.id.wallets)
    RecyclerView mWallets;

    private List<WalletMeta> mWalletMetas;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdd.setOnClickListener(v -> mPresenter.onAdd());

        mWallets.setLayoutManager(new LinearLayoutManager(getActivity()));
        WalletsAdapter adapter = new WalletsAdapter((RootActivity) getActivity(), mWalletMetas);
        mWallets.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        BACK_PATH.clear();
        BACK_PATH.setScreen(WALLETS);
    }

    @Override
    int getLayout() {
        return R.layout.fragment_wallets;
    }

    @Override
    WalletsPresenter createPresenter() {
        return new WalletsPresenter();
    }

    @Override
    BaseToolbar createToolbar(View view) {
        return new BaseSettingsToolbar(view, NETWORK_TYPE_MAINNET == APP_CONTEXT.getNetworkType() ? R.string.title_wallets : R.string.title_wallets_stagenet, (RootActivity) getActivity()) {
            @Override
            protected void onButton() {
                ((RootActivity) getActivity()).show(SettingsFragment.newInstance());
            }
        };
    }

    private BaseFragment showWallets(List<WalletMeta> result) {
        mWalletMetas = result;
        return this;
    }

    public static BaseFragment newInstance(List<WalletMeta> result) {
        return new WalletsFragment().showWallets(result);
    }
}