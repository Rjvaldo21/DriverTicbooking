package com.app.driverticbooking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Tangani pesan masuk (baik notifikasi maupun data payload)
        if (remoteMessage.getNotification() != null) {
            // Mengambil pesan notifikasi
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            // Tampilkan notifikasi atau proses sesuai kebutuhan
            showNotification(title, body);
        }

        // Jika terdapat data payload
        if (remoteMessage.getData().size() > 0) {
            // Proses data payload sesuai kebutuhan aplikasi Anda
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Kirim token ke server Anda jika diperlukan untuk pengiriman notifikasi terarah
        Log.d("FCM", "Token baru: " + token);
    }

    private void showNotification(String title, String message) {
        // Contoh pembuatan notifikasi menggunakan NotificationCompat
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Untuk Android Oreo ke atas, buat channel notifikasi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default_channel",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());
    }
}

