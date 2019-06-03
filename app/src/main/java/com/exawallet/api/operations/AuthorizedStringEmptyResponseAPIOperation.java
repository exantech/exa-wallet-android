package com.exawallet.api.operations;

import com.exawallet.api.ISigner;
import com.exawallet.api.listeners.IBaseSequenceListener;

abstract class AuthorizedStringEmptyResponseAPIOperation<L extends IBaseSequenceListener> extends AuthorizedStringAPIOperation<Object, L> {
    AuthorizedStringEmptyResponseAPIOperation(L listener, String sessionId, int nonce, String secretKey, ISigner signer) {
        super(listener, sessionId, nonce, secretKey, signer);
    }

    @Override
    final Class<Object> getClassOfValue() {
        return null;
    }
}
