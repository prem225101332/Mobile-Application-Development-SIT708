package com.example.personaleventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EventListFragment extends Fragment {

    private EventAdapter adapter;
    private EventDB db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.eventslist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = EventDB.getInstance(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new EventAdapter(event -> {
            Bundle bundle = new Bundle();
            bundle.putLong("eventId", event.getId());
            Navigation.findNavController(view)
                    .navigate(R.id.action_list_to_detail, bundle);
        });

        recyclerView.setAdapter(adapter);

        db.eventDao().getAllEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.setEvents(events);

            View emptyState = view.findViewById(R.id.emptyState);
            if (events == null || events.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.addeditevent));
    }
}