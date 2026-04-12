package com.example.istreamapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.istreamapp.R;
import com.example.istreamapp.adapter.PlaylistAdapter;
import com.example.istreamapp.database.AppDatabase;
import com.example.istreamapp.database.PlaylistItem;
import com.example.istreamapp.session.SessionManager;
import com.example.istreamapp.viewmodel.SharedViewModel;

public class PlaylistFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SessionManager session = new SessionManager(requireContext());
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        TextView tvEmpty = view.findViewById(R.id.tvEmpty);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        tvUsername.setText(session.getUsername() + "'s Playlist");

        PlaylistAdapter adapter = new PlaylistAdapter(
                item -> {
                    sharedViewModel.setSelectedUrl(item.videoUrl);
                    NavHostFragment.findNavController(this).navigateUp();
                },
                item -> {
                    new Thread(() -> db.playlistDao().remove(item)).start();
                }
        );

        RecyclerView rv = view.findViewById(R.id.rvPlaylist);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        db.playlistDao().getPlaylistForUser(session.getUserId()).observe(getViewLifecycleOwner(), items -> {
                    adapter.setItems(items);
                    tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                });

        view.findViewById(R.id.btnBack).setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            session.logout();
            NavHostFragment.findNavController(this).navigate(R.id.action_playlist_to_login);
        });
    }
}