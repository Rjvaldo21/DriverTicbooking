package com.app.driverticbooking;

import static com.app.driverticbooking.MyFirebaseMessagingService.SERVER_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import static com.app.driverticbooking.MyFirebaseMessagingService.SERVER_URL;
import static com.app.driverticbooking.SessionManager.KEY_EMAIL;

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
                    getDeviceToken();
                }else{
                    Log.w(TAG, "❌ Autoriza notifikasaun rejeita husi user.");
                }
            }
    );

    private void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "❌ Falha hetan token FCM", task.getException());
                        return;
                    }

                    // **Dapatkan token FCM baru**
                    String token = task.getResult();
                    Log.d(TAG, "✅ Token FCM hetan ona: " + token);

                    // **Simpan token ke SharedPreferences**
                    SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fcm_token", token);
                    editor.apply();

                    String storedToken = sharedPreferences.getString("fcm_token", "La existe");
                    Log.d(TAG, "📌 Token FCM salva iha SharedPreferences: " + storedToken);
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
        editor.commit();

        Log.d("LoginActivity", "🔑 Token Django salva ona: " + authToken);

        Log.d("LoginActivity", "🚀 Bolu sendFCMTokenAfterLogin()");
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
        Log.d(TAG, "🔥 Komesa manda token FCM...");
        Log.d(TAG, "📡 Endpoint: " + SERVER_URL);
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String userAuthToken = sharedPreferences.getString("auth_token", "");

        // Log auth token
        Log.d(TAG, "🔑 Auth Token: " + userAuthToken);
        Log.d(TAG, "📡 Haruka token FCM ba server: " + SERVER_URL);
        Log.d(TAG, "🔑 Token nebe manda ona: " + token);

        if (token == null || token.trim().isEmpty()) {
            Log.e(TAG, "❌ Token FCM mamuk, la manda ba server!");
            return;
        }

        if (userAuthToken == null || userAuthToken.trim().isEmpty()) { // Ganti ke camelCase
            Log.e(TAG, "❌ Falha manda token FCM: Token autentikasaun la disponivel!");
            return;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token", token);
            jsonBody.put("title", "Test Notifikasaun");
            jsonBody.put("body", "Mensajen husi Android ba Django!");
            JSONObject data = new JSONObject();
            data.put("promo", "Ex.Diskontu 50%");
            jsonBody.put("data", data);
        } catch (JSONException e) {}
            {
            // Handle error
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(SERVER_URL)
                .header("Authorization", "Token " + userAuthToken)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.e(TAG, "❌ Falha manda token FCM: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "❌ Falha manda token FCM! Status Code: " + response.code());
                        return;
                    }

                    String responseBody = response.body() != null ? response.body().string() : "No Response Body";
                    Log.d(TAG, "✅ Responde husi server: " + response.code() + " - " + responseBody);

                } catch (IOException e) {
                    Log.e(TAG, "❌ Erru lee responde husi server: " + e.getMessage());
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
            Toast.makeText(this, "Naran & password tenke prenxe", Toast.LENGTH_SHORT).show();
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

                    sessionManager.saveUserName(userName);
                    sessionManager.saveToken(token);
                    sessionManager.setLoggedIn(true);

                    Log.d("LoginActivity", "📧 Email nebe simu husi API: " + email);
                    Log.d("LoginActivity", "👤 UserName: " + userName);
                    Log.d("LoginActivity", "🔑 Token: " + token);

                    // ✅ Panggil getUser setelah dapat token
                    Retrofit retrofit = RetrofitClient.getClient();
                    ApiService apiService = retrofit.create(ApiService.class);

                    apiService.getUser("Token " + token).enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                UserResponse user = response.body();

                                String email = user.getEmail();
                                sessionManager.saveEmail(email);

                                sessionManager.saveEmail(user.getEmail());
                                Log.d("SessionManager", "✅ Salva ona email: " + email + " ke key: " + KEY_EMAIL);

                                sessionManager.saveUserName(user.getFullName());
                                sessionManager.saveDepartement(user.getDepartement());
                                sessionManager.saveRole(user.getRole());


                                UserResponse.Vehicle v = user.getVehicle();
                                if (v != null) {
                                    sessionManager.saveVehicleName(v.getName());
                                    sessionManager.saveVehicleType(v.getType());
                                    sessionManager.saveVehicleStatus(v.getStatus());
                                    sessionManager.saveVehicleCapacity(v.getCapacity());
                                }

                                Log.d("LoginActivity", "✅ Email salva ona ba SessionManager: " + user.getEmail());
                                Log.d("LoginActivity", "📦 Email nebe salva ona: " + user.getEmail());

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    Toast.makeText(LoginActivity.this, "Login susesu!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }, 1500);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            Log.e("API", "Falha foti data user: " + t.getMessage());
                        }
                    });

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