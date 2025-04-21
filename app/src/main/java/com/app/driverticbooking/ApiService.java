package com.app.driverticbooking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("user/")
    Call<UserResponse> getUserProfile(@Header("Authorization") String token);

    @GET("user/")
    Call<UserResponse> getUser(@Header("Authorization") String authToken);

    @GET("vehicle-bookings/")
    Call<VehicleBookingResponse> getVehicleBookings(@Header("Authorization") String token);

    @GET("executive-meetings/")
    Call<ExecutiveMeetingResponse> getExecutiveMeetings(@Header("Authorization") String token);

    @GET("notifications/")
    Call<NotificationResponse> getNotification(
    @Header("Authorization") String auth,
    @Query("type") String all
    );
}
