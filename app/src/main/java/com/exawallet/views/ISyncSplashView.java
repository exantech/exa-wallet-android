package com.exawallet.views;

public interface ISyncSplashView extends IConnectionView {
    void setProgress(long blocks);
}