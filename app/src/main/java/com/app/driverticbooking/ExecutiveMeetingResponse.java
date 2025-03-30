package com.app.driverticbooking;

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

        public int getId() { return id; }
        public String getRequesterName() { return requesterName; }
        public String getLocation() { return location; }
        public String getPurposeName() { return purposeName; }
        public String getFormattedStartTime() { return formattedStartTime; }
        public String getFormattedEndTime() { return formattedEndTime; }
        public String getStatus() { return status; }
    }
}
