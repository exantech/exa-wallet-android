package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.ChartPresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.WalletManageToolbar;
import com.exawallet.views.IChartView;
import com.exawallet.widget.Chart;

import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.CHART;
import static com.exawallet.utils.Utils.formatAmountB;
import static com.exawallet.utils.Utils.formatBalanceTicker;

public class ChartFragment extends BaseSyncWalletFragment<ChartPresenter, IChartView, BaseHomeToolbar> implements IChartView {
    @BindView(R.id.balance)
    TextView mBalance;

    @BindView(R.id.balance_ticker)
    TextView mBalanceTicker;

    @BindView(R.id.ticker)
    Spinner mTicker;

    @BindView(R.id.period)
    Spinner mPeriod;

    @BindView(R.id.chart_fab)
    Chart mChart;

    @Override
    int getLayout() {
        return R.layout.fragment_chart_view;
    }

    @Override
    ChartPresenter createPresenter() {
        return new ChartPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChart.setDataProvider(mPresenter.getDataProvider());

        ArrayAdapter<String> tickerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mPresenter.getTickers());
        tickerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTicker.setAdapter(tickerAdapter);
        mTicker.setSelection(2);
        mTicker.setOnItemSelectedListener(mPresenter.onTickerSelected());

        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mPresenter.getPeriods());
        tickerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPeriod.setAdapter(periodAdapter);
        mPeriod.setSelection(4);
        mPeriod.setOnItemSelectedListener(mPresenter.onPeriodSelected());

        BACK_PATH.setScreen(CHART);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.update(walletManager().getWallet());
    }

    @Override
    protected BaseHomeToolbar createToolbar(View view) {
        return new WalletManageToolbar(view, R.string.title_exchange_rates, (RootActivity) getActivity()) {

            @Override
            protected void onButton() {
                ((RootActivity) getActivity()).show(WalletManageFragment.newInstance());
            }
        };
    }

    @Override
    public void showBalance(long balance) {
        mBalance.setText(formatAmountB(balance));
    }

    @Override
    public void showBalanceTicker(double balance, String ticker) {
        mBalanceTicker.setText(formatBalanceTicker(balance, ticker));
    }

    @Override
    public void notifyUpdate() {
        if (null != mChart) {
            mChart.postInvalidate();
        }
    }

    public static ChartFragment newInstance() {
        return new ChartFragment();
    }
}