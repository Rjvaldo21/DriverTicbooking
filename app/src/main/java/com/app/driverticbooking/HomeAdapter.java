package com.app.driverticbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // ✅ Import TextView

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MeetingViewHolder> {
    private List<ExecutiveMeetingResponse.ExecutiveMeeting> meetings;

    public HomeAdapter(List<ExecutiveMeetingResponse.ExecutiveMeeting> meetings) {
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_executive_booking, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        ExecutiveMeetingResponse.ExecutiveMeeting meeting = meetings.get(position);

        if (meeting != null) {
            holder.bind(meeting);
        } else {
            android.util.Log.e("HomeAdapter", "Meeting object at position " + position + " is null!");
        }
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    static class MeetingViewHolder extends RecyclerView.ViewHolder {
        TextView meetingTitle, meetingDate, requesterName, location, purpose, startTime, endTime, status;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            requesterName = itemView.findViewById(R.id.tvRequester);
            location = itemView.findViewById(R.id.tvLocation);
            purpose = itemView.findViewById(R.id.tvPurpose);
            startTime = itemView.findViewById(R.id.tvStartTime);
            endTime = itemView.findViewById(R.id.tvEndTime);
            status = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(ExecutiveMeetingResponse.ExecutiveMeeting meeting) {
            requesterName.setText("Requester: " + meeting.getRequesterName());
            location.setText("Location: " + meeting.getLocation());
            purpose.setText("Purpose: " + meeting.getPurposeName());
            startTime.setText("Start: " + meeting.getFormattedStartTime());
            endTime.setText("End: " + meeting.getFormattedEndTime());
            status.setText("Status: " + meeting.getStatus());
        }
    }
}

