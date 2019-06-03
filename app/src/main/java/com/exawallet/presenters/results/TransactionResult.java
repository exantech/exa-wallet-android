package com.exawallet.presenters.results;

import com.exawallet.engine.results.ExceptionResult;

public class TransactionResult extends ExceptionResult<String[]> {

    public TransactionResult(Exception exception) {
        super(exception);
    }

    public TransactionResult(String[] result) {
        super(result);
    }
}