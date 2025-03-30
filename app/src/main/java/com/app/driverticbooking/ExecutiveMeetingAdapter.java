package com.app.driverticbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExecutiveMeetingAdapter extends RecyclerView.Adapter<ExecutiveMeetingAdapter.ViewHolder> {

    private final List<ExecutiveMeetingResponse.ExecutiveMeeting> meetings;

    public ExecutiveMeetingAdapter(List<ExecutiveMeetingResponse.ExecutiveMeeting> meetings) {
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_executive_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExecutiveMeetingResponse.ExecutiveMeeting meeting = meetings.get(position);

        holder.tvRequester.setText("Requester: " + meeting.getRequesterName());
        holder.tvLocation.setText("Location: " + meeting.getLocation());
        holder.tvPurpose.setText("Purpose: " + meeting.getPurposeName());
        holder.tvTime.setText("Time: " + meeting.getFormattedStartTime() + " - " + meeting.getFormattedEndTime());
        holder.tvStatus.setText("Status: " + meeting.getStatus());
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequester, tvLocation, tvPurpose, tvTime, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequester = itemView.findViewById(R.id.tvRequester);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
