package com.app.driverticbooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;
    private TextView cardSubtitle, tvEmail, tvLogout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleViewAdapter;
    private RecyclerView.LayoutManager recycleViewLayoutManager;
    private ArrayList<ItemModel> data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(getContext());

        tvEmail = root.findViewById(R.id.tvEmail);
        cardSubtitle = root.findViewById(R.id.cardSubtitle);
        tvLogout = root.findViewById(R.id.tvView);
        recyclerView = root.findViewById(R.id.recycleView);
        Toolbar toolbar = root.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String userEmail = sessionManager.getEmail();
        if (userEmail == null || userEmail.trim().isEmpty() || userEmail.equals("No Email")) {
            userEmail = "Email not available";
        }
        tvEmail.setText(userEmail);
        Log.d("ProfileFragment", "📧 Email yang ditampilkan di ProfileFragment: " + userEmail);

        String userName = sessionManager.getUserName();
        Log.d("ProfileFragment", "👤 UserName: " + userName);
        updateUserProfile(userName);

        setupRecyclerView();

        tvLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return root;
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recycleViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recycleViewLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        data = new ArrayList<>();
        for (int i = 0; i < MyItem.Headline.length; i++) {
            data.add(new ItemModel(MyItem.Headline[i], MyItem.Subheadline[i], MyItem.iconlist[i]));
        }

        recycleViewAdapter = new MyRecycleView(data);
        recyclerView.setAdapter(recycleViewAdapter);
    }

    private void updateUserProfile(String userName) {
        if (cardSubtitle != null) {
            if (userName != null && !userName.isEmpty()) {
                cardSubtitle.setText(userName);
            } else {
                cardSubtitle.setText("Welcome, Guest!");
            }
        }
    }
}
