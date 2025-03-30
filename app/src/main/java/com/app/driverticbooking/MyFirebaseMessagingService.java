package com.app.driverticbooking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM_Service";
    public static final String SERVER_URL = "https://ticbooking-system.apps06.tic.gov.tl/api/register-token/";
    // URL API Django

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "🆕 Token FCM diterima: " + token);

        // Simpan token ke SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcm_token", token);
        editor.apply();

        Log.d(TAG, "📌 Token FCM disimpan di SharedPreferences: " + token);

        // Cek apakah user sudah login sebelum mengirim token ke server
        String userAuthToken = sharedPreferences.getString("auth_token", "");
        if (!userAuthToken.isEmpty()) {
            sendTokenToServer(token);
        } else {
            Log.w(TAG, "⚠️ Pengguna belum login. Token FCM akan dikirim nanti.");
        }
    }

    private void sendTokenToServer(String token) {
        Log.d("API Request", "📡 Mengirim Token FCM ke server...");

        if (token == null || token.trim().isEmpty()) {
            Log.e("API Request", "❌ Token FCM kosong, tidak dikirim ke server!");
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String USER_AUTH_TOKEN = sharedPreferences.getString("auth_token", "");

        if (USER_AUTH_TOKEN == null || USER_AUTH_TOKEN.trim().isEmpty()) {
            Log.e("API Request", "❌ Gagal mengirim token FCM: Token autentikasi tidak ditemukan!");
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token", token);
            jsonBody.put("title", "Test Notifikasi");
            jsonBody.put("body", "Pesan dari Android ke Django!");

            // Data tambahan (contoh promo)
            JSONObject dataObject = new JSONObject();
            dataObject.put("promo", "Diskon 50%");
            jsonBody.put("data", dataObject);

        } catch (JSONException e) {
            Log.e("API Request", "❌ JSON Error: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(SERVER_URL)
                .header("Authorization", "Token " + USER_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "❌ Gagal mengirim token: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    Log.d(TAG, "🔄 Respon Server: " + response.code());

                    if (response.body() != null) {
                        String responseBody = response.body().string();
                        Log.d(TAG, "📩 Body Respon: " + responseBody);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "❌ Error membaca respon: " + e.getMessage());
                } finally {
                    // Tutup response untuk hindari memory leak
                    if (response != null) {
                        response.close();
                    }
                }
            }
        });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "📩 Pesan masuk: " + remoteMessage.getData());

        String title = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getTitle() : "Notifikasi Baru";
        String message = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "Anda punya pesan baru.";

        showNotification(title, message);
    }

    private void showNotification(String title, String message) {
        String CHANNEL_ID = "FCM_CHANNEL";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "FCM Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Pastikan icon ini ada di drawable
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

        notificationManager.notify(101, builder.build());
    }
}

