package com.exawallet.api.listeners;

import com.exawallet.api.results.SessionId;

public interface IOpenSessionListener extends IBaseSequenceListener {
    void onOpenSession(SessionId result);
}