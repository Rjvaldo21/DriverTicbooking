package com.app.driverticbooking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchExecutiveMeetings();  // Panggil di sini
    }

    // Method diletakkan DI LUAR onViewCreated
    private void fetchExecutiveMeetings() {
        String token = new SessionManager(requireContext()).getToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = RetrofitClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getExecutiveMeetings(token).enqueue(new Callback<ExecutiveMeetingResponse>() {
            @Override
            public void onResponse(Call<ExecutiveMeetingResponse> call, Response<ExecutiveMeetingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ExecutiveMeetingResponse.ExecutiveMeeting> meetings = response.body().getResults();

                    if (!meetings.isEmpty()) {
                        RecyclerView recyclerExecutiveMeetings = requireView().findViewById(R.id.recyclerExecutiveMeetings);
                        recyclerExecutiveMeetings.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerExecutiveMeetings.setAdapter(new ExecutiveMeetingAdapter(meetings));
                    }
                } else {
                    Log.e("API Error", "Failed to load executive meetings: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ExecutiveMeetingResponse> call, Throwable t) {
                Log.e("API Failure", "Request failed: " + t.getMessage());
                Toast.makeText(requireContext(), "Failed to load executive meetings: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        SupportMapFragment mapFragment = (SupportMapFragment)
//        getChildFragmentManager().findFragmentById(R.id.map);
//
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng dili = new LatLng(-8.5569, 125.5603);
        mMap.addMarker(new MarkerOptions().position(dili).title("Dili, Timor-Leste"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dili, 12));
    }
}