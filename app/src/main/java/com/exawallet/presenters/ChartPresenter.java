package com.exawallet.presenters;

import android.view.View;
import android.widget.AdapterView;
import com.exawallet.api.listeners.IObtainCandleListener;
import com.exawallet.api.listeners.IObtainTickerListener;
import com.exawallet.api.results.HitBtcCandle;
import com.exawallet.api.results.HitBtcTicker;
import com.exawallet.common.WalletRefreshed;
import com.exawallet.model.Wallet;
import com.exawallet.presenters.utils.ChartDataProviderImpl;
import com.exawallet.views.IChartView;
import com.exawallet.widget.ChartDataProvider;
import org.greenrobot.eventbus.Subscribe;

import static com.exawallet.api.operations.ObtainCandlesOperation.obtainCandles;
import static com.exawallet.api.operations.ObtainTickerOperation.obtainTicker;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class ChartPresenter extends BaseSyncWalletPresenter<IChartView> {

    ChartDataProviderImpl mChartDataProvider = new ChartDataProviderImpl();

    @Subscribe(threadMode = MAIN)
    public void onEvent(final WalletRefreshed event) {
        update(mWallet);
    }

    public void update(Wallet wallet) {
        mView.showBalance(mWallet.getUnlockedBalance());

        obtainTicker(mChartDataProvider.getTicker(), new TickerListener() {
            @Override
            public void onObtainTicker(HitBtcTicker result) {
                mView.showBalanceTicker(mWallet.getUnlockedBalance() * result.getLast(), mChartDataProvider.getXmrTicker());
            }
        });

        obtainCandles(mChartDataProvider.getTicker(), mChartDataProvider.getPeriod(), new CandlesListener() {
            @Override
            public void onObtainCandles(HitBtcCandle[] result) {
                mChartDataProvider.setCandles(result);

                mView.notifyUpdate();
            }
        });
    }

    public ChartDataProvider getDataProvider() {
        return mChartDataProvider;
    }

    public AdapterView.OnItemSelectedListener onTickerSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChartDataProvider.setTicker(mChartDataProvider.getTickers()[position]);

                update(mWallet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    public String[] getTickers() {
        return mChartDataProvider.getTickerNames();
    }

    public String[] getPeriods() {
        return mChartDataProvider.getPeriodNames();
    }

    public AdapterView.OnItemSelectedListener onPeriodSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChartDataProvider.setPeriod(mChartDataProvider.getPeriods()[position]);

                update(mWallet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    private abstract class TickerListener implements IObtainTickerListener {
        @Override
        public final void onFailure(Exception exception) {
        }
    }

    private abstract class CandlesListener implements IObtainCandleListener {
        @Override
        public final void onFailure(Exception exception) {
        }
    }
}