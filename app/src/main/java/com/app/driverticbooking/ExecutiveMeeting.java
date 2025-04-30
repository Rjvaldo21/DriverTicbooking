//package com.app.driverticbooking;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
//public class ExecutiveMeeting {
//    @SerializedName("id")
//    private int id;
//
//    @SerializedName("requester_name")
//    private String requesterName;
//
//    @SerializedName("location")
//    private String location;
//
//    @SerializedName("purpose_name")
//    private String purposeName;
//
//    @SerializedName("formatted_start_time")
//    private String formattedStartTime;
//
//    @SerializedName("formatted_end_time")
//    private String formattedEndTime;
//
//    @SerializedName("participants_users")
//    private List<String> participantsUsers;
//
//    @SerializedName("participants_departments")
//    private List<String> participantsDepartments;
//
//    @SerializedName("substitute_executive")
//    private List<String> substituteExecutive;
//
//    @SerializedName("room")
//    private ExecutiveMeetingResponse.Room room;
//
//    @SerializedName("obs")
//    private String obs;
//
//    // Tambahkan getter-nya juga:
//    public List<String> getParticipantsUsers() { return participantsUsers; }
//    public List<String> getParticipantsDepartments() { return participantsDepartments; }
//    public List<String> getSubstituteExecutive() { return substituteExecutive; }
//    public ExecutiveMeetingResponse.Room getRoom() { return room; }
//    public String getObs() { return obs; }
//
//    @SerializedName("status")
//    private String status;
//
//    @SerializedName("vehicle")
//    private Vehicle vehicle;
//
//    public int getId() {
//        return id;
//    }
//
//    public String getRequesterName() {
//        return requesterName;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public String getPurposeName() {
//        return purposeName;
//    }
//
//    public String getFormattedStartTime() {
//        return formattedStartTime;
//    }
//
//    public String getFormattedEndTime() {
//        return formattedEndTime;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public Vehicle getVehicle() {
//        return vehicle;
//    }
//
//    // Alternatif untuk title dan date
//    public String getTitle() {
//        return requesterName; // Gunakan nama pemohon sebagai judul
//    }
//
//    public String getDate() {
//        return formattedStartTime; // Gunakan waktu mulai sebagai tanggal
//    }
//
//    public static class Vehicle {
//        @SerializedName("name")
//        private String name;
//
//        @SerializedName("driver_name")
//        private String driverName;
//
//        public String getName() {
//            return name;
//        }
//
//        public String getDriverName() {
//            return driverName;
//        }
//    }
//}
