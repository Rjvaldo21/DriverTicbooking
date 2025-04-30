package com.app.driverticbooking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ExecutiveMeetingResponse {

    @SerializedName("count")
    private int count;

    @SerializedName("results")
    private List<ExecutiveMeeting> results;

    public int getCount() {
        return count;
    }

    public List<ExecutiveMeeting> getResults() {
        return results;
    }

    public static class ExecutiveMeeting {
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

        @SerializedName("participants_users")
        private List<String> participantsUsers;

        public List<String> getParticipantsUsers() {
            return participantsUsers;
        }

        @SerializedName("participants_departments")
        private List<String> participantsDepartments;

        @SerializedName("substitute_executive")
        private List<String> substituteExecutive;

        @SerializedName("room")
        private Room room;

        @SerializedName("vehicle")
        private Vehicle vehicle;

        @SerializedName("obs")
        private String obs;

        public int getId() { return id; }
        public String getRequesterName() { return requesterName; }
        public String getLocation() { return location; }
        public String getPurposeName() { return purposeName; }
        public String getFormattedStartTime() { return formattedStartTime; }
        public String getFormattedEndTime() { return formattedEndTime; }
        public String getStatus() { return status; }
        public List<String> getParticipantsDepartments() { return participantsDepartments; }
        public List<String> getSubstituteExecutive() { return substituteExecutive; }
        public Room getRoom() { return room; }
        public Vehicle getVehicle() { return vehicle; }
        public String getObs() { return obs; }
    }

    public static class Room {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        public int getId() { return id; }
        public String getName() { return name; }
    }

    public static class Vehicle {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("driver_name")
        private String driverName;

        public int getId() { return id; }
        public String getName() { return name; }
        public String getDriverName() { return driverName; }
    }
}

