package com.app.driverticbooking;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExecutiveMeetingAdapter extends RecyclerView.Adapter<ExecutiveMeetingAdapter.ViewHolder> {

    private final List<ExecutiveMeetingResponse.ExecutiveMeeting> meetings;
    private final SparseBooleanArray expandedPositions = new SparseBooleanArray();

    public ExecutiveMeetingAdapter(List<ExecutiveMeetingResponse.ExecutiveMeeting> meetings) {
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_executive_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExecutiveMeetingResponse.ExecutiveMeeting meeting = meetings.get(position);

        // Haketak Data & Oras
        String start = meeting.getFormattedStartTime();
        String end = meeting.getFormattedEndTime();

        String[] startParts = start.split(" ");
        String[] endParts = end.split(" ");

        holder.tvDateSummary.setText(startParts[0]);
        holder.tvStartTimeSummary.setText(startParts[1]);

        holder.tvDate.setText(startParts[0]);
        holder.tvEndDate.setText(endParts[0]);
        holder.tvStartTime.setText(startParts[1]);
        holder.tvEndTime.setText(endParts[1]);

        holder.tvRequester.setText(meeting.getRequesterName());
        holder.tvLocation.setText(meeting.getLocation() != null ? meeting.getLocation() : "-");
        holder.tvLocations.setText(meeting.getLocation() != null ? meeting.getLocation() : "-");
        holder.tvPurpose.setText(meeting.getPurposeName());
        holder.tvStatus.setText(meeting.getStatus());
        holder.tvObs.setText(meeting.getObs() != null ? meeting.getObs() : "-");

        holder.tvDepartments.setText(
                meeting.getParticipantsDepartments() != null && !meeting.getParticipantsDepartments().isEmpty()
                        ? TextUtils.join(", ", meeting.getParticipantsDepartments()) : "-"
        );

        holder.tvUsers.setText(
                meeting.getParticipantsUsers() != null && !meeting.getParticipantsUsers().isEmpty()
                        ? TextUtils.join(", ", meeting.getParticipantsUsers()) : "-"
        );

        holder.tvSubstitutes.setText(
                meeting.getSubstituteExecutive() != null && !meeting.getSubstituteExecutive().isEmpty()
                ? TextUtils.join(", ", meeting.getSubstituteExecutive()) : "-"
        );

        holder.tvRoom.setText(meeting.getRoom() != null ? meeting.getRoom().getName() : "-");

        if (meeting.getVehicle() != null && meeting.getVehicle().getName() != null) {
            String vehicleInfo = meeting.getVehicle().getName();
            if (meeting.getVehicle().getDriverName() != null) {
                vehicleInfo += " (" + meeting.getVehicle().getDriverName() + ")";
            }
            holder.tvVehicle.setText(vehicleInfo);
        } else {
            holder.tvVehicle.setText("-");
        }

    // Expand/collapse logic
        boolean isExpanded = expandedPositions.get(position, false);
        holder.expandableSection.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.ivChevron.setRotation(isExpanded ? 180f : 0f);

        int summaryVisibility = isExpanded ? View.GONE : View.VISIBLE;
        holder.tvDateSummary.setVisibility(summaryVisibility);
        holder.tvStartTimeSummary.setVisibility(summaryVisibility);
        holder.labelDateSummary.setVisibility(summaryVisibility);
        holder.labelStartTimeSummary.setVisibility(summaryVisibility);

        holder.ivChevron.setOnClickListener(v -> {
            boolean currentlyExpanded = expandedPositions.get(position, false);
            expandedPositions.put(position, !currentlyExpanded);
            notifyItemChanged(position);
        });
    }


    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequester, tvLocation, tvPurpose, tvEndDate, tvDate, tvStartTime, tvEndTime, tvStatus;
        TextView tvDepartments, tvSubstitutes, tvUsers, tvRoom, tvVehicle, tvObs;
        ImageView ivChevron;TextView tvDateSummary, tvStartTimeSummary, tvLocations;
        TextView labelDateSummary, labelStartTimeSummary;
        View expandableSection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateSummary = itemView.findViewById(R.id.tvDateSummary);
            tvLocations = itemView.findViewById(R.id.tvLocations);
            tvStartTimeSummary = itemView.findViewById(R.id.tvStartTimeSummary);
            labelDateSummary = itemView.findViewById(R.id.labelDateSummary);
            labelStartTimeSummary = itemView.findViewById(R.id.labelStartTimeSummary);
            tvRequester = itemView.findViewById(R.id.tvRequester);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDepartments = itemView.findViewById(R.id.tvDepartments);
            tvUsers = itemView.findViewById(R.id.tvUsers);
            tvSubstitutes = itemView.findViewById(R.id.tvSubstitutes);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvObs = itemView.findViewById(R.id.tvObs);
            ivChevron = itemView.findViewById(R.id.ivChevron);
            expandableSection = itemView.findViewById(R.id.expandableContainer);
        }
    }
}
