package com.app.driverticbooking;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApplication extends Application {
    private static final String TAG = "FirebaseInit";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "🔥 Mulai inisialisasi Firebase...");

        try {
            FirebaseApp.initializeApp(this);
            Log.d(TAG, "✅ Firebase berhasil diinisialisasi!");

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "❌ Gagal mendapatkan token FCM", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.d(TAG, "✅ Token FCM: " + token);
                    });
        } catch (Exception e) {
            Log.e(TAG, "❌ Error saat inisialisasi Firebase", e);
        }
    }
}
