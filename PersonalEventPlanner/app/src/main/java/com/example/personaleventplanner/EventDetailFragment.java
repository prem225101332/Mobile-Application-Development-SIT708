package com.example.personaleventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import java.util.concurrent.Executors;

public class EventDetailFragment extends Fragment {

    private EventDB db;
    private long eventId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.eventdetail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = EventDB.getInstance(requireContext());

        TextView tvTitle = view.findViewById(R.id.tvDetailTitle);
        TextView tvCategory = view.findViewById(R.id.tvDetailCategory);
        TextView tvDate = view.findViewById(R.id.tvDetailDate);
        TextView tvTime = view.findViewById(R.id.tvDetailTime);
        TextView tvLocation = view.findViewById(R.id.tvDetailLocation);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        if (getArguments() != null) {
            eventId = getArguments().getLong("eventId", -1);
        }

        db.eventDao().getEventById(eventId).observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                tvTitle.setText(event.getTitle());
                tvCategory.setText(event.getCategory());
                tvDate.setText(event.getDate());
                tvTime.setText(event.getTime());
                tvLocation.setText(event.getLocation());
            }
        });

        btnEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("eventId", eventId);
            Navigation.findNavController(view)
                    .navigate(R.id.action_detail_to_edit, bundle);
        });

        btnDelete.setOnClickListener(v -> {
            db.eventDao().getEventById(eventId).observe(getViewLifecycleOwner(), event -> {
                if (event != null) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        db.eventDao().delete(event);
                        requireActivity().runOnUiThread(() -> {
                            Snackbar.make(view, "Event deleted!", Snackbar.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigateUp();
                        });
                    });
                }
            });
        });
    }
}