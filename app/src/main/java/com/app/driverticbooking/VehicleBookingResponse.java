package com.app.driverticbooking;

import java.util.List;

public class VehicleBookingResponse {
    private int count;
    private String next;
    private String previous;
    private List<VehicleBooking> results;

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<VehicleBooking> getResults() {
        return results;
    }
}
