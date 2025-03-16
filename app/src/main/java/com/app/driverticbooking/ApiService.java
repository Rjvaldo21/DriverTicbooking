package com.app.driverticbooking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("user/")
    Call<UserResponse> getUserProfile(@Header("Authorization") String token);

    @GET("vehicle-bookings/")
    Call<VehicleBookingResponse> getVehicleBookings(@Header("Authorization") String token);
}
