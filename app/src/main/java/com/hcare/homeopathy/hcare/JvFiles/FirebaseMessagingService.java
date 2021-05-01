package com.hcare.homeopathy.hcare.JvFiles;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.google.firebase.messaging.RemoteMessage;
import com.hcare.homeopathy.hcare.R;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.Objects;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from_user_id = remoteMessage.getData().get("from_user_id");
        String notification_title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String notification_message = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();

      //  Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
      //  builder.setSound(sound);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.vector_doctor)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message)
                        .setAutoCancel(true)
                        //.setSound(sound)
                ;

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("user_id", from_user_id);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancelAll();
    }
}