package com.app.driverticbooking;

import static com.app.driverticbooking.MyFirebaseMessagingService.SERVER_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import static com.app.driverticbooking.MyFirebaseMessagingService.SERVER_URL;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "FCM";
    private ActivityResultLauncher<String> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted){
                    // Permission Granted, ambil token
                    getDeviceToken();
                }else{
                    Log.w(TAG, "❌ Izin notifikasi ditolak oleh user.");
                }
            }
    );

    private void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "❌ Gagal mendapatkan token FCM", task.getException());
                        return;
                    }

                    // **Dapatkan token FCM baru**
                    String token = task.getResult();
                    Log.d(TAG, "✅ Token FCM didapatkan: " + token);

                    // **Simpan token ke SharedPreferences**
                    SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fcm_token", token);
                    editor.apply();

                    // **Verifikasi apakah token benar-benar tersimpan**
                    String storedToken = sharedPreferences.getString("fcm_token", "Tidak Ada");
                    Log.d(TAG, "📌 Token FCM tersimpan di SharedPreferences: " + storedToken);
                });
    }

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        sessionManager = new SessionManager(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resultLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        }

        btnLogin.setOnClickListener(view -> loginUser());
    }

    private void onLoginSuccess(String authToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", authToken);
        editor.commit(); // Ganti apply() dengan commit()

        Log.d("LoginActivity", "🔑 Token Django disimpan: " + authToken);

        Log.d("LoginActivity", "🚀 Memanggil sendFCMTokenAfterLogin()");
        sendFCMTokenAfterLogin();
    }


    private void sendFCMTokenAfterLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String tokenFCM = sharedPreferences.getString("fcm_token", null);

        if (tokenFCM == null) {
            // Generate token jika belum ada
            getDeviceToken();
            tokenFCM = sharedPreferences.getString("fcm_token", null);
        }

        if (tokenFCM != null && !tokenFCM.isEmpty()) {
            sendTokenToServer(tokenFCM);
        }
    }

    private void sendTokenToServer(String token) {
        // Ambil USER_AUTH_TOKEN terlebih dahulu
        Log.d(TAG, "🔥 Memulai pengiriman token FCM...");
        Log.d(TAG, "📡 Endpoint: " + SERVER_URL);
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String userAuthToken = sharedPreferences.getString("auth_token", ""); // Ganti ke camelCase

        // Log auth token
        Log.d(TAG, "🔑 Auth Token: " + userAuthToken);
        Log.d(TAG, "📡 Mengirim token FCM ke server: " + SERVER_URL);
        Log.d(TAG, "🔑 Token yang dikirim: " + token);

        if (token == null || token.trim().isEmpty()) {
            Log.e(TAG, "❌ Token FCM kosong, tidak mengirim ke server!");
            return;
        }

        if (userAuthToken == null || userAuthToken.trim().isEmpty()) { // Ganti ke camelCase
            Log.e(TAG, "❌ Gagal mengirim token FCM: Token autentikasi tidak ditemukan!");
            return;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token", token);
            jsonBody.put("title", "Test Notifikasi");
            jsonBody.put("body", "Pesan dari Android ke Django!");
            JSONObject data = new JSONObject();
            data.put("promo", "Diskon 50%");
            jsonBody.put("data", data);
        } catch (JSONException e) {}
            {
            // Handle error
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(SERVER_URL)
                .header("Authorization", "Token " + userAuthToken) // <-- PASTIKAN INI userAuthToken
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.e(TAG, "❌ Gagal mengirim token FCM: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "❌ Gagal mengirim token FCM! Status Code: " + response.code());
                        return;
                    }

                    String responseBody = response.body() != null ? response.body().string() : "No Response Body";
                    Log.d(TAG, "✅ Respon dari server: " + response.code() + " - " + responseBody);

                } catch (IOException e) {
                    Log.e(TAG, "❌ Error membaca respon dari server: " + e.getMessage());
                } finally {
                    response.close(); // Hindari memory leak
                }
            }
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Naran & password presiza", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = RetrofitClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(username, password);
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String email = response.body().getEmail();
                    String userName = response.body().getUserName();
                    String token = response.body().getToken();

                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                    sessionManager.saveEmail(email);
                    sessionManager.saveUserName(userName);
                    sessionManager.saveToken(token);

                    Log.d("LoginActivity", "📧 Email yang diterima dari API: " + email);
                    Log.d("LoginActivity", "👤 UserName: " + userName);
                    Log.d("LoginActivity", "🔑 Token: " + token);

                    Toast.makeText(LoginActivity.this, "Login susesu!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login falha!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erru: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}