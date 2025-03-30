package com.app.driverticbooking;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifikasaun");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        View decor = getWindow().getDecorView();
        getWindow().setStatusBarColor(Color.WHITE);
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample JSON response (Replace with actual API response)
        String json = "{ \"data\": [ { \"id\": 1, \"payload\": { \"title\": \"Test Notification\", \"body\": \"This is a test notification from Django.\", \"icon\": \"https://your-website.com/static/icons/notification.png\", \"sound\": null, \"click_action\": null, \"badge\": null, \"data\": null }, \"is_read\": false, \"created_at\": \"2025-02-27T00:49:09.819403Z\" } ] }";

        // Parse JSON using Gson
        Gson gson = new GsonBuilder().create();
        NotificationResponse response = gson.fromJson(json, NotificationResponse.class);
        fetchAllExe();
        // Set adapter with parsed notifications

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void fetchAllExe() {
        SessionManager sessionManager = new SessionManager(this);
        String token = sessionManager.getToken();
        String auth = "Token " + token;
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.getNotification(auth, "all").enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationItem> notifications = response.body().getData();
                    adapter = new NotificationAdapter(notifications);
                    recyclerView.setAdapter(adapter);
                } else {
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
            }
        });
    }
}

