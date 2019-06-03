package com.exawallet.presenters.results;

import com.exawallet.engine.results.ExceptionResult;
import com.exawallet.model.Wallet;

public class WalletResult extends ExceptionResult<Wallet> {

    public WalletResult(Exception exception) {
        super(exception);
    }

    public WalletResult(Wallet result) {
        super(result);
    }
}
