package com.example.sportsfeedapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sportsfeedapp.R;
import com.example.sportsfeedapp.FeaturedAdapter;
import com.example.sportsfeedapp.NewsAdapter;
import com.example.sportsfeedapp.DataSource;
import com.example.sportsfeedapp.NewsItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class HomeFragment extends Fragment {

    private FeaturedAdapter featuredAdapter;
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        featuredAdapter = new FeaturedAdapter(this::navigateToDetail);
        RecyclerView rvFeatured = view.findViewById(R.id.rvFeatured);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFeatured.setLayoutManager(horizontalManager);
        rvFeatured.setAdapter(featuredAdapter);
        rvFeatured.setNestedScrollingEnabled(false);
        featuredAdapter.setItems(DataSource.getFeatured());

        newsAdapter = new NewsAdapter(this::navigateToDetail);
        RecyclerView rvLatest = view.findViewById(R.id.rvLatest);
        rvLatest.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLatest.setAdapter(newsAdapter);
        rvLatest.setNestedScrollingEnabled(false);
        newsAdapter.setItems(DataSource.getLatest());

        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
        String[] categories = {"All", "Football", "Basketball", "Cricket"};
        for (String cat : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(cat);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setOnCheckedChangeListener((btn, checked) -> {if (checked) applyFilter(cat);
            });
            chipGroup.addView(chip);
        }
        ((Chip) chipGroup.getChildAt(0)).setChecked(true);

        ImageButton btnBookmarks = view.findViewById(R.id.btnBookmarks);
        btnBookmarks.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_home_to_bookmarks));
    }

    private void applyFilter(String category) {
        featuredAdapter.setItems(DataSource.getFeaturedByCategory(category));
        newsAdapter.setItems(DataSource.getByCategory(category));
    }

    private void navigateToDetail(NewsItem item) {
        Bundle args = new Bundle();
        args.putInt("newsId", item.getId());
        NavHostFragment.findNavController(this).navigate(R.id.action_home_to_detail, args);
    }
}