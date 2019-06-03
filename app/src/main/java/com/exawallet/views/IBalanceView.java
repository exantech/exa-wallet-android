package com.exawallet.views;

import com.exawallet.model.TransactionInfo;
import com.exawallet.model.meta.TxProposalsMeta;

import java.util.List;

public interface IBalanceView extends ISynchronizationView {

    void setSyncStatus(int image);

    void showBalance(long balance);

    void showBalanceUsd(double balance);

    void showBalanceUnconfirmed(long balance);

    void showSync(long blockNumber);

    void showTransactions(List<TransactionInfo> transactions);

    void showTxProposals(boolean isMultisig, List<TxProposalsMeta> txProposalsMeta);

    void setWalletType(int text);
}