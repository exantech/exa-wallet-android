package com.exawallet.api.requests;

public class FCMTokenInfo {
    final String platform = "FCM";
    final String token;
    final String device_uid;

    public FCMTokenInfo(String token, String deviceUid) {
        this.token = token;
        this.device_uid = deviceUid;
    }
}