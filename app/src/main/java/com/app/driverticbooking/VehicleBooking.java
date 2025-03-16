package com.app.driverticbooking;

import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class VehicleBooking {
    private int id;
    @SerializedName("resource_type")
    private String resourceType;
    private String description;
    @SerializedName("destination_address")
    private String destinationAddress;
    @SerializedName("requester_name_details")
    private String requesterNameDetails;

    @SerializedName("start_time")
    private String startTime;  // contoh: "2025-02-18T01:13:31Z"

    @SerializedName("end_time")
    private String endTime;    // contoh: "2025-02-18T03:13:32Z"

    @SerializedName("purpose_details")
    private PurposeDetails purposeDetails;

    @SerializedName("departement_details")
    private DepartementDetails departementDetails;

    @SerializedName("vehicle_details")
    private VehicleDetails vehicleDetails;

    private String status;

    // Metode helper untuk mendapatkan start date (yyyy-MM-dd)
    public String getFormattedStartDate() {
        return formatDate(startTime, "yyyy-MM-dd");
    }

    // Metode helper untuk mendapatkan start time (HH:mm)
    public String getFormattedStartTime() {
        return formatDate(startTime, "HH:mm");
    }

    // Metode helper untuk mendapatkan end date (yyyy-MM-dd)
    public String getFormattedEndDate() {
        return formatDate(endTime, "yyyy-MM-dd");
    }

    // Metode helper untuk mendapatkan end time (HH:mm)
    public String getFormattedEndTime() {
        return formatDate(endTime, "HH:mm");
    }

    // Metode umum untuk memformat string tanggal dengan konversi ke waktu lokal
    private String formatDate(String rawDate, String outputPattern) {
        if (rawDate == null || rawDate.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getDefault());

            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    // Getter
    public int getId() { return id; }
    public String getResourceType() { return resourceType; }
    public String getDescription() { return description; }
    public String getDestinationAddress() { return destinationAddress; }
    public String getRequesterNameDetails() { return requesterNameDetails; }
    public PurposeDetails getPurposeDetails() { return purposeDetails; }
    public DepartementDetails getDepartementDetails() { return departementDetails; }
    public VehicleDetails getVehicleDetails() { return vehicleDetails; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }

    // Inner classes
    public static class PurposeDetails {
        private int id;
        private String name;
        public int getId() { return id; }
        public String getName() { return name; }
    }

    public static class DepartementDetails {
        private int id;
        private String name;
        public int getId() { return id; }
        public String getName() { return name; }
    }

    public static class VehicleDetails {
        @SerializedName("name")
        private String name;

        @SerializedName("type")
        private String type;

        @SerializedName("capacity")
        private int capacity;

        @SerializedName("status")
        private String status;

        @SerializedName("driver_name")
        private String driverName;

        @SerializedName("driver")
        private int driver;

        public String getName() { return name != null ? name : "No Name"; }
        public String getType() { return type != null ? type : "No Type"; }
        public int getCapacity() { return capacity; }
        public String getStatus() { return status != null ? status : "No Status"; }
        public String getDriverName() { return driverName != null ? driverName : "No Driver"; }
        public int getDriver() { return driver; }
    }
}
