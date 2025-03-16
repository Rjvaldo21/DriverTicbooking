package com.app.driverticbooking;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements VehicleBookingAdapter.OnMapIconClickListener {

    private RecyclerView recyclerView;
    private VehicleBookingAdapter adapter;
    private SessionManager sessionManager;
    private TextView cardSubtitle, vehicleName, vehicleType, vehicleCapacity, vehicleStatus;
    private AppBarLayout appBarLayout;
    private CardView cardInfo;
    private LinearLayout toolbarTitleLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        appBarLayout = root.findViewById(R.id.appBarLayout);
        cardInfo = root.findViewById(R.id.cardInfo);
        toolbarTitleLayout = root.findViewById(R.id.toolbarTitleLayout);
        ImageView greetingBackground = root.findViewById(R.id.greetingBackground);
        recyclerView = root.findViewById(R.id.recyclerView);
        cardSubtitle = root.findViewById(R.id.cardSubtitle);
        vehicleName = root.findViewById(R.id.tvVehicleName);
//        vehicleType = root.findViewById(R.id.tvVehicleType);
//        vehicleCapacity = root.findViewById(R.id.tvVehicleCapacity);
        vehicleStatus = root.findViewById(R.id.tvVehicleStatus);

        sessionManager = new SessionManager(requireContext());

        String greetingMessage = getGreetingMessage();

        String userName = sessionManager.getUserName();

        updateUserName(greetingMessage, userName);

        updateGreetingBackground(greetingBackground);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchBookings();

        fetchBookings(vehicleName, vehicleType, vehicleCapacity, vehicleStatus);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                if (Math.abs(verticalOffset) == totalScrollRange) {
                    if (cardInfo != null) cardInfo.setVisibility(View.GONE);
                    if (toolbarTitleLayout != null) toolbarTitleLayout.setVisibility(View.VISIBLE);
                } else {
                    if (cardInfo != null) cardInfo.setVisibility(View.VISIBLE);
                    if (toolbarTitleLayout != null) toolbarTitleLayout.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }

    private void fetchBookings(TextView vehicleName, TextView vehicleType, TextView vehicleCapacity, TextView vehicleStatus) {
        String token = sessionManager.getToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = RetrofitClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getUserProfile(token).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    UserResponse.Vehicle vehicle = user.getVehicle();

                    Log.d("DEBUG API", "📩 Full API Response: " + new Gson().toJson(user));
                    Log.d("DEBUG API", "User Data: " + new Gson().toJson(user));

                    if (vehicle != null) {
                        Log.d("DEBUG VEHICLE", "Vehicle Name: " + vehicle.getName());
                        Log.d("DEBUG VEHICLE", "Vehicle Type: " + vehicle.getType());
                        Log.d("DEBUG VEHICLE", "Vehicle Capacity: " + vehicle.getCapacity());
                        Log.d("DEBUG VEHICLE", "Vehicle Status: " + vehicle.getStatus());

                        if (isAdded() && getActivity() != null) {
                            requireActivity().runOnUiThread(() -> {
                                if (vehicleName != null) vehicleName.setText("" + vehicle.getName());
                                if (vehicleType != null) vehicleType.setText("" + vehicle.getType());
                                if (vehicleCapacity != null) vehicleCapacity.setText("" + vehicle.getCapacity());
                                if (vehicleStatus != null) vehicleStatus.setText("" + vehicle.getStatus());
                            });
                        }
                    } else {
                        Log.e("DEBUG VEHICLE", "No vehicle assigned!");
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vehicleName.setText("No vehicle assigned");
                                vehicleType.setText("");
                                vehicleCapacity.setText("");
                                vehicleStatus.setText("");
                            }
                        });
                    }
                } else {
                    Log.e("DEBUG API", "Failed to load user data: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("DEBUG API", "Error: " + t.getMessage());
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

        @Override
        public void onMapIconClicked(String destinationAddress) {
            DashboardFragment dashboardFragment = new DashboardFragment();
            Bundle bundle = new Bundle();
            bundle.putString("destination_address", destinationAddress);
            dashboardFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, dashboardFragment)
            .addToBackStack(null)
            .commit();
        }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getWindow().setStatusBarColor(
                getResources().getColor(R.color.navyBlue, null)
        );
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_VISIBLE
        );
    }

    private String getGreetingMessage() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Dili"));

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (hour >= 5 && hour < 12) {
            return "Bom Dia!";
        } else if ((hour == 18 && minute < 50) || (hour >= 12 && hour < 18)) {
            return "Boa Tarde!";
        } else {
            return "Boa Noite!";
        }
    }

    private void updateUserName(String greetingMessage, String userName) {
        if (cardSubtitle != null) {
            if (userName != null && !userName.isEmpty()) {
                cardSubtitle.setText(greetingMessage + ", " + userName + "...");
            } else {
                cardSubtitle.setText(greetingMessage + ", Guest!");
            }
        }
    }

    private void updateGreetingBackground(ImageView greetingBackground) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 12) {
            greetingBackground.setImageResource(R.drawable.ic_morning);
        } else if (hour >= 12 && hour < 15) {
            greetingBackground.setImageResource(R.drawable.ic_evening);
        } else if (hour >= 15 && hour < 18) {
            greetingBackground.setImageResource(R.drawable.ic_afternoon);
        } else {
            greetingBackground.setImageResource(R.drawable.ic_night);
        }
    }

    private void fetchBookings() {
        String token = sessionManager.getToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("API Request", "Fetching bookings with token: " + token);

        Retrofit retrofit = RetrofitClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getVehicleBookings(token).enqueue(new Callback<VehicleBookingResponse>() {
            @Override
            public void onResponse(Call<VehicleBookingResponse> call, Response<VehicleBookingResponse> response) {
                Log.d("API Response", "Status Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", "Raw JSON: " + new Gson().toJson(response.body()));

                    List<VehicleBooking> bookings = response.body().getResults();

                    if (!bookings.isEmpty()) {
                        String driverName = bookings.get(0).getVehicleDetails().getDriverName();
                        Log.d("Driver Name", "Driver: " + driverName);

                        if (driverName != null && !driverName.isEmpty()) {
                            sessionManager.saveUserName(driverName);

                            String greetingMessage = getGreetingMessage();
                            updateUserName(greetingMessage, driverName);
                        }
                    }

                    adapter = new VehicleBookingAdapter(bookings, HomeFragment.this);
                    recyclerView.setAdapter(adapter);

                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error Message: " + errorMessage);
                        Toast.makeText(requireContext(), "Error: " + response.code() + " - " + errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Error parsing response", e);
                        Toast.makeText(requireContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleBookingResponse> call, Throwable t) {
                Log.e("API Failure", "Request failed: " + t.getMessage());
                Toast.makeText(requireContext(), "Failed to load data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}