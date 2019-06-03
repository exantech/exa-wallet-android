package com.exawallet.views;

public interface IChartView extends ISynchronizationView {
    void showBalance(long balance);

    void showBalanceTicker(double balance, String ticker);

    void notifyUpdate();
}
