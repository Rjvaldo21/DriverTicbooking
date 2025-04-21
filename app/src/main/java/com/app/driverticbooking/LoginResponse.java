package com.app.driverticbooking;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String token;

    @SerializedName("username")
    private String userName;

    @SerializedName("email")
    private String email;

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
