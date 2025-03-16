package com.app.driverticbooking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {

    // 🔹 Format waktu dari API: "2025-03-13T08:07:00Z"
    public static String formatDate(String rawDate, String outputPattern) {
        if (rawDate == null || rawDate.isEmpty()) {
            return "Tanggal tidak tersedia";
        }
        try {
            // 🔥 Parsing dari format API ke Date
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Pastikan sesuai dengan API

            // 🔥 Format output sesuai dengan zona waktu lokal
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getDefault()); // Gunakan zona waktu lokal perangkat

            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Format waktu salah";
        }
    }

    // 🔹 Format hanya untuk tanggal (dd MMM yyyy)
    public static String getFormattedStartDate(String startTime) {
        return formatDate(startTime, "dd MMM yyyy");  // Output: 13 Mar 2025
    }

    // 🔹 Format hanya untuk waktu (HH:mm)
    public static String getFormattedStartTime(String startTime) {
        return formatDate(startTime, "HH:mm");  // Output: 08:07
    }

    public static String getFormattedEndDate(String endTime) {
        return formatDate(endTime, "dd MMM yyyy");  // Output: 13 Mar 2025
    }

    public static String getFormattedEndTime(String endTime) {
        return formatDate(endTime, "HH:mm");  // Output: 11:07
    }
}
