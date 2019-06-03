package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IBaseSequenceListener;

abstract class AuthorizedJsonEmptyResponseAPIOperation<O, L extends IBaseSequenceListener> extends AuthorizedJsonAPIOperation<Object, O, L> {
    AuthorizedJsonEmptyResponseAPIOperation(L listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
    }

    @Override
    final Class<Object> getClassOfValue() {
        return null;
    }
}