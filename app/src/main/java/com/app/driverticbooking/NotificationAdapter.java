package com.app.driverticbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.driverticbooking.MyApplication;
import com.bumptech.glide.Glide;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationItem> notifications;

    public NotificationAdapter(List<NotificationItem> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notification = notifications.get(position);

        // Set title & body
        holder.tvTitle.setText(notification.getPayload().getTitle());
        holder.tvBody.setText(notification.getPayload().getBody());
        holder.tvDate.setText(notification.getCreatedAt().substring(0, 10)); // Extract date

        // Load icon using Glide (default image if null)
        String iconUrl = notification.getPayload().getIcon();
        Glide.with(holder.itemView.getContext())
                .load(iconUrl != null ? iconUrl : R.drawable.ic_notification)
                .placeholder(R.drawable.ic_notification)
                .into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBody, tvDate;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvBody = itemView.findViewById(R.id.tvNotificationBody);
            tvDate = itemView.findViewById(R.id.tvNotificationDate);
            ivIcon = itemView.findViewById(R.id.ivNotificationIcon);
        }
    }
}
