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

        holder.tvDepartments.setText("Departments: " +
                (meeting.getParticipantsDepartments().isEmpty() ? "-" : String.join(", ", meeting.getParticipantsDepartments()))
        );
        holder.tvSubstitutes.setText("Substitutes: " +
                (meeting.getSubstituteExecutive().isEmpty() ? "-" : String.join(", ", meeting.getSubstituteExecutive()))
        );
        holder.tvRoom.setText("Room: " + (meeting.getRoom() != null ? meeting.getRoom().getName() : "-"));
        holder.tvVehicle.setText("Vehicle: " + (meeting.getVehicle() != null ? meeting.getVehicle().getName() + " (" + meeting.getVehicle().getDriverName() + ")" : "-"));
        holder.tvObs.setText("Observations: " + meeting.getObs());

    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequester, tvLocation, tvPurpose, tvTime, tvStatus;
        TextView tvDepartments, tvSubstitutes, tvRoom, tvVehicle, tvObs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequester = itemView.findViewById(R.id.tvRequester);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDepartments = itemView.findViewById(R.id.tvDepartments);
            tvSubstitutes = itemView.findViewById(R.id.tvSubstitutes);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvObs = itemView.findViewById(R.id.tvObs);
        }
    }
}
