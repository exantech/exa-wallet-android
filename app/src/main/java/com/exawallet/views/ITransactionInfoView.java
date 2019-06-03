package com.exawallet.views;

import com.exawallet.model.TransactionInfo;

public interface ITransactionInfoView extends ISynchronizationView {
    void showTransactionInfo(TransactionInfo transactionInfo);
}
