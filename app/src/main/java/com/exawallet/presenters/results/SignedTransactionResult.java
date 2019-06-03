package com.exawallet.presenters.results;

import com.exawallet.engine.results.ExceptionResult;

public class SignedTransactionResult extends ExceptionResult<String> {
    public SignedTransactionResult(String result) {
        super(result);
    }

    public SignedTransactionResult(Exception exception) {
        super(exception);
    }
}