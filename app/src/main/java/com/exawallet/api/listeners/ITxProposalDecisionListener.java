package com.exawallet.api.listeners;

public interface ITxProposalDecisionListener extends IBaseSequenceListener {
    void onTxProposalDecision(String id);
}