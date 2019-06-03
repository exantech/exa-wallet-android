package com.exawallet.messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.exawallet.RootActivity;
import com.exawallet.monerowallet.R;
import com.exawallet.utils.HLog;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.common.AppContext.getCurrentWalletAddress;

public class ExaFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "ExaFirebaseMessagingService";
    private static final String WALLET_ID = "wallet_id";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        HLog.debug(TAG, "onNewToken: " + token);

        APP_CONTEXT.setFCMToken(token);
        APP_CONTEXT.save(getFilesDir());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();

        String walletId = data.containsKey(WALLET_ID) ? String.valueOf(data.get(WALLET_ID)) : WALLET_ID;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(walletId, "Wallet notification channel", NotificationManager.IMPORTANCE_DEFAULT));
        }

        if (isEmpty(getCurrentWalletAddress())) {
            notificationManager.notify(0, new NotificationCompat.Builder(this, walletId).
                    setSmallIcon(R.drawable.ic_exawallet).
                    setContentText(data.containsKey("type") && "new_proposal".equals(data.get("type")) ? "Incoming proposal" : "Wallet has been updated").
                    setAutoCancel(true).
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).
                    setContentIntent(PendingIntent.getActivity(this, 0 /* Request code */, new Intent(this, RootActivity.class), PendingIntent.FLAG_ONE_SHOT)).
                    build());
        }
    }
}