package com.exawallet.presenters.results;

import com.exawallet.engine.results.ExceptionResult;
import com.exawallet.model.meta.WalletMeta;

import java.util.List;

public class ContentResult extends ExceptionResult<List<WalletMeta>> {
    public ContentResult(Exception exception) {
        super(exception);
    }

    public ContentResult(List<WalletMeta> result) {
        super(result);
    }
}