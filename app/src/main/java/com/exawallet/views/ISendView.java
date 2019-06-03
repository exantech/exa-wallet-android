package com.exawallet.views;

public interface ISendView extends ISynchronizationView {
    void setAddressError(String message);

    void setAmountError(String message);

    void setPaymentIdError(String message);

    void setDescriptionError(String message);

    void enableSendButton(boolean isEnable);

    void setAddressTo(String addressTo);

    void setPaimentId(String paimentId);

    void setPaymentNoteHint(String paymentNoteHint);

    void showErrorHint(String string);
}