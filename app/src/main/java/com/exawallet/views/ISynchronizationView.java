package com.exawallet.views;

public interface ISynchronizationView extends IConnectionView {
    void notifySynchronized(boolean isSynchronized);
}
