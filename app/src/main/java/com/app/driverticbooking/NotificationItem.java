package com.app.driverticbooking;

public class NotificationItem {
    private int id;
    private NotificationPayload payload;
    private boolean is_read;
    private String created_at;

    public int getId() {
        return id;
    }

    public NotificationPayload getPayload() {
        return payload;
    }

    public boolean isRead() {
        return is_read;
    }

    public String getCreatedAt() {
        return created_at;
    }
}

