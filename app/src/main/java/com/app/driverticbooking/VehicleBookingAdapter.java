package com.app.driverticbooking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VehicleBookingAdapter extends RecyclerView.Adapter<VehicleBookingAdapter.ViewHolder> {

    private final List<VehicleBooking> bookings;
    private boolean[] expandedStates;
    private OnMapIconClickListener listener;

    public interface OnMapIconClickListener {
        void onMapIconClicked(String destinationAddress);
    }

    public VehicleBookingAdapter(List<VehicleBooking> bookings, OnMapIconClickListener listener) {
        this.bookings = bookings;
        this.expandedStates = new boolean[bookings.size()];
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VehicleBooking booking = bookings.get(position);
        Context context = holder.itemView.getContext();

        if (booking == null) {
            Log.e("VehicleBookingAdapter", "Booking data is null at position: " + position);
            return;
        }

        setTextView(holder.tvPurpose, booking.getPurposeDetails() != null ? booking.getPurposeDetails().getName() : "No Purpose");
        setTextView(holder.tvDestination, booking.getDestinationAddress() != null ? booking.getDestinationAddress() : "No Destination");
        setTextView(holder.tvStartDate, booking.getFormattedStartDate());
        setTextView(holder.tvStartTime, booking.getFormattedStartTime());

        setTextView(holder.tvStartDatesAlt, booking.getFormattedStartDate());
        setTextView(holder.tvStartTimesAlt, booking.getFormattedStartTime());

        boolean isExpanded = expandedStates[position];

        holder.detailsContainer.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        holder.expandableSection.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.ivChevron.setImageResource(isExpanded ? R.drawable.ic_chevron_up : R.drawable.ic_chevron_down);

        holder.ivChevron.setOnClickListener(v -> {
            expandedStates[position] = !expandedStates[position];

            if (expandedStates[position]) {
                holder.detailsContainer.setVisibility(View.GONE);
                holder.expandableSection.setVisibility(View.VISIBLE);

                // ✅ Set data tambahan
                setTextView(holder.tvEndDate, booking.getFormattedEndDate());
                setTextView(holder.tvEndTime, booking.getFormattedEndTime());
                setTextView(holder.tvRequesterName, booking.getRequesterNameDetails());
                setTextView(holder.tvDepartement, booking.getDepartementDetails() != null ? booking.getDepartementDetails().getName() : "No Department");
                setTextView(holder.tvDriverName, booking.getVehicleDetails() != null ? booking.getVehicleDetails().getDriverName() : "No Driver");
                setTextView(holder.tvDescription, booking.getDescription());
            } else {
                holder.detailsContainer.setVisibility(View.VISIBLE);
                holder.expandableSection.setVisibility(View.GONE);
            }

            holder.ivChevron.setImageResource(expandedStates[position] ? R.drawable.ic_chevron_up : R.drawable.ic_chevron_down);
        });

        holder.ivMapIcon.setOnClickListener(v -> {
            String destination = booking.getDestinationAddress();
            if (destination != null && !destination.isEmpty()) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(destination));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                } else {
                    Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookings.size();
    }

    private void setTextView(TextView textView, String text) {
        if (textView != null && text != null) {
            textView.setText(text);
        } else if (textView != null) {
            textView.setText("N/A");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPurpose, tvDescription, tvDestination, tvRequesterName, tvDepartement, tvDriverName;
        TextView tvStartTime, tvEndTime, tvStatus, tvStartDate, tvEndDate;
        TextView tvStartDatesAlt, tvStartTimesAlt;
        ImageView ivChevron, ivMapIcon;
        LinearLayout expandableSection;
        ConstraintLayout detailsContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvRequesterName = itemView.findViewById(R.id.tvRequesterName);
            tvDepartement = itemView.findViewById(R.id.tvDepartement);
            tvDriverName = itemView.findViewById(R.id.tvDriverName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvStartDatesAlt = itemView.findViewById(R.id.tvStartDatesAlt);
            tvStartTimesAlt = itemView.findViewById(R.id.tvStartTimesAlt);
            ivChevron = itemView.findViewById(R.id.ivChevron);
            ivMapIcon = itemView.findViewById(R.id.ivMapIcon);
            expandableSection = itemView.findViewById(R.id.expandableSection);
            detailsContainer = itemView.findViewById(R.id.detailsContainer);

            if (ivChevron == null) {
                Log.e("ViewHolder", "ivChevron is NULL! Check XML.");
            }
        }
    }
}