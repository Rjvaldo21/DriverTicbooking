package com.app.driverticbooking;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("user_id")
    private int userId;

    @SerializedName("username")
    private String username;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role;

    @SerializedName("departement")
    private String departement;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    // Getter methods
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getDepartement() { return departement; }
    public Vehicle getVehicle() { return vehicle; }

    public static class Vehicle {
        @SerializedName("name")
        private String name;

        @SerializedName("type")
        private String type;

        @SerializedName("capacity")
        private int capacity;

        @SerializedName("status")
        private String status;

        public String getName() { return name != null ? name : "No Name"; }
        public String getType() { return type != null ? type : "No Type"; }
        public int getCapacity() { return capacity; }
        public String getStatus() { return status != null ? status : "No Status"; }
    }
}

