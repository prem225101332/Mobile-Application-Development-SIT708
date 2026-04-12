package com.example.sportsfeedapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sportsfeedapp.R;
import com.example.sportsfeedapp.BookmarkAdapter;
import com.example.sportsfeedapp.NewsItem;
import com.example.sportsfeedapp.AppDatabase;
import com.example.sportsfeedapp.Bookmark;
import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppDatabase db = AppDatabase.getDatabase(requireContext());

        BookmarkAdapter adapter = new BookmarkAdapter(item -> {
            Bundle args = new Bundle();
            args.putInt("newsId", item.getId());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_bookmarks_to_detail, args);
        });

        RecyclerView rv = view.findViewById(R.id.rvBookmarks);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        TextView tvEmpty = view.findViewById(R.id.tvEmpty);

        db.bookmarkDao().getAllBookmarks().observe(getViewLifecycleOwner(), bookmarks -> {
            List<NewsItem> items = new ArrayList<>();
            for (Bookmark b : bookmarks) {
                items.add(new NewsItem(b.id, b.title, b.description, b.imageRes, b.category, false));
            }
            adapter.setItems(items);
            tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        });

        view.findViewById(R.id.btnBack).setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }
}