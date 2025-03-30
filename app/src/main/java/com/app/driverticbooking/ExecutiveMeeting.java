package com.app.driverticbooking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExecutiveMeeting {
    @SerializedName("id")
    private int id;

    @SerializedName("requester_name")
    private String requesterName;

    @SerializedName("location")
    private String location;

    @SerializedName("purpose_name")
    private String purposeName;

    @SerializedName("formatted_start_time")
    private String formattedStartTime;

    @SerializedName("formatted_end_time")
    private String formattedEndTime;

    @SerializedName("status")
    private String status;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    public int getId() {
        return id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public String getLocation() {
        return location;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    public String getFormattedEndTime() {
        return formattedEndTime;
    }

    public String getStatus() {
        return status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    // Alternatif untuk title dan date
    public String getTitle() {
        return requesterName; // Gunakan nama pemohon sebagai judul
    }

    public String getDate() {
        return formattedStartTime; // Gunakan waktu mulai sebagai tanggal
    }

    public static class Vehicle {
        @SerializedName("name")
        private String name;

        @SerializedName("driver_name")
        private String driverName;

        public String getName() {
            return name;
        }

        public String getDriverName() {
            return driverName;
        }
    }
}
