package com.app.driverticbooking;

public class Vehicle {
    private int id;
    private boolean in_use;
    private String driver_name;
    private String name;
    private String type;
    private int capacity;
    private String status;
    private int driver;

    // Getter
    public String getDriverName() { return driver_name; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getCapacity() { return capacity; }
    public String getStatus() { return status; }
}
