package com.exawallet.views;

import android.graphics.Bitmap;

public interface IInviteCodeView extends IBaseView {
    void setInviteCode(String inviteCode);

    void showQRImage(Bitmap bitmap);

    void setParticipantsProgress(int progress, int count);
}