package com.app.driverticbooking;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS";
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{POST_NOTIFICATIONS}, 101);
            }
        }

        bottomNavigation = findViewById(R.id.bottomNavigation);

        loadFragment(new HomeFragment());
        sendTokenToServer();

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_calendar) {
                selectedFragment = new DashboardFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
    private void sendTokenToServer() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
         String SERVER_URL = "https://ticbooking-system.apps06.tic.gov.tl/api/register-token/";
        String token = sharedPreferences.getString("fcm_token", "");
        String USER_AUTH_TOKEN = new SessionManager(this).getToken();

        Log.d("API Request", "📡 Mengirim Token FCM ke server...");
        Log.d("API Request", token);

        if (token == null || token.trim().isEmpty()) {
            Log.e("API sendTokenToServer", "❌ Token FCM kosong, tidak dikirim ke server!");
            return;
        }


        if (USER_AUTH_TOKEN == null || USER_AUTH_TOKEN.trim().isEmpty()) {
            Log.e("API sendTokenToServer", "❌ Gagal mengirim token FCM: Token autentikasi tidak ditemukan!");
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
            Log.e("API sendTokenToServer", "❌ JSON Error: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(SERVER_URL)
                .header("Authorization", USER_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        // 1. Inisialisasi Logging Interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Atur level logging ke BODY

        // 2. Buat OkHttpClient baru menggunakan Builder dan tambahkan Interceptor
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging) // Menambahkan interceptor ke client
                .build();

        // 3. Eksekusi permintaan (tetap sama)
        client.newCall(request).enqueue(new Callback() {
            // ... (onFailure dan onResponse)
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "❌ Gagal mengirim token: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                // ... (Kode penanganan respon)
                try {
                    Log.d(TAG, "🔄 Respon sendTokenToServer: " + response.code());

                    if (response.body() != null) {
                        String responseBody = response.body().string();
                        Log.d(TAG, "📩 Body Respon: " + responseBody);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "❌ Error membaca sendTokenToServer: " + e.getMessage());
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
