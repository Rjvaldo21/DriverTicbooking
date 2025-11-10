package com.app.driverticbooking;

import com.google.gson.annotations.SerializedName;

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

    // ISO dari backend (UTC atau offset)
    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    // Formatted dari backend (contoh: "dd/MM/yyyy HH:mm")
    @SerializedName("formatted_start_time")
    private String formattedStartTime;

    @SerializedName("formatted_end_time")
    private String formattedEndTime;

    @SerializedName("purpose_details")
    private PurposeDetails purposeDetails;

    @SerializedName("departement_details")
    private DepartementDetails departementDetails;

    @SerializedName("vehicle_details")
    private VehicleDetails vehicleDetails;

    private String status;

    // =========================
    //  Konfigurasi format
    // =========================
    private static final Locale LOCALE_ID = new Locale("id", "ID");
    private static final TimeZone TZ_ASIA_DILI = TimeZone.getTimeZone("Asia/Dili");
    private static final TimeZone TZ_UTC = TimeZone.getTimeZone("UTC");

    // Pola ISO umum dari backend
    private static final String[] ISO_PATTERNS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ss",   // tanpa offset -> asumsi UTC
            "yyyy-MM-dd'T'HH:mm"       // tanpa detik -> asumsi UTC
    };

    // Pola formatted_* dari backend (sesuaikan dengan serializer server)
    private static final String[] FMT_PATTERNS = new String[]{
            "dd/MM/yyyy HH:mm",
            "d/M/yyyy HH:mm",
            "dd-MM-yyyy HH:mm",
            "d-M-yyyy HH:mm"
    };

    // =========================
    //  Public getters untuk UI
    // =========================

    /** Tanggal mulai lokal Asia/Dili, output "yyyy-MM-dd". */
    public String getFormattedStartDate() {
        // 1) coba formatted_* dari backend (cepat & konsisten)
        String v = fromFormattedTo("DATE", formattedStartTime);
        if (!v.isEmpty()) return v;

        // 2) fallback parse ISO
        return fromIsoTo(startTime, "yyyy-MM-dd");
    }

    /** Jam mulai lokal Asia/Dili, output "HH:mm". */
    public String getFormattedStartTime() {
        String v = fromFormattedTo("TIME", formattedStartTime);
        if (!v.isEmpty()) return v;

        return fromIsoTo(startTime, "HH:mm");
    }

    /** Tanggal selesai lokal Asia/Dili, output "yyyy-MM-dd". */
    public String getFormattedEndDate() {
        String v = fromFormattedTo("DATE", formattedEndTime);
        if (!v.isEmpty()) return v;

        return fromIsoTo(endTime, "yyyy-MM-dd");
    }

    /** Jam selesai lokal Asia/Dili, output "HH:mm". */
    public String getFormattedEndTime() {
        String v = fromFormattedTo("TIME", formattedEndTime);
        if (!v.isEmpty()) return v;

        return fromIsoTo(endTime, "HH:mm");
    }

    // =========================
    //  Helper formatter
    // =========================

    private static String fromIsoTo(String iso, String outPattern) {
        if (iso == null || iso.trim().isEmpty()) return "";
        for (String pat : ISO_PATTERNS) {
            try {
                SimpleDateFormat src = new SimpleDateFormat(pat, Locale.US);
                // jika pattern tidak punya offset/zone, anggap UTC
                if (!pat.contains("XXX") && !pat.contains("'Z'")) {
                    src.setTimeZone(TZ_UTC);
                }
                Date d = src.parse(iso);
                if (d != null) {
                    SimpleDateFormat out = new SimpleDateFormat(outPattern, LOCALE_ID);
                    out.setTimeZone(TZ_ASIA_DILI);
                    return out.format(d);
                }
            } catch (Exception ignore) {}
        }
        return "";
    }

    /** Parse formatted_* dari backend lalu reformat ke kebutuhan UI. */
    private static String fromFormattedTo(String kind, String formatted) {
        if (formatted == null || formatted.trim().isEmpty()) return "";
        for (String pat : FMT_PATTERNS) {
            try {
                SimpleDateFormat src = new SimpleDateFormat(pat, LOCALE_ID);
                // formatted_* dari server adalah waktu lokal server (kita anggap Asia/Dili)
                src.setTimeZone(TZ_ASIA_DILI);
                Date d = src.parse(formatted);
                if (d != null) {
                    String outPattern = "DATE".equals(kind) ? "yyyy-MM-dd" : "HH:mm";
                    SimpleDateFormat out = new SimpleDateFormat(outPattern, LOCALE_ID);
                    out.setTimeZone(TZ_ASIA_DILI);
                    return out.format(d);
                }
            } catch (Exception ignore) {}
        }
        return "";
    }

    // =========================
    //  Getter original
    // =========================
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

    // =========================
    //  Inner classes
    // =========================
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
