// fragment/DetailFragment.java
package com.example.sportsfeedapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sportsfeedapp.R;
import java.util.List;

public class DetailFragment extends Fragment {

    private AppDatabase db;
    private NewsItem currentItem;
    private ImageButton btnBookmark;
    private boolean isBookmarked = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = AppDatabase.getDatabase(requireContext());

        int newsId = getArguments() != null ? getArguments().getInt("newsId", -1) : -1;
        currentItem = DataSource.getById(newsId);
        if (currentItem == null) return;

        Glide.with(this).load(currentItem.getImageRes()).into((ImageView) view.findViewById(R.id.ivDetail));
                
        ((TextView) view.findViewById(R.id.tvDetailCategory)).setText(currentItem.getCategory());
        ((TextView) view.findViewById(R.id.tvDetailTitle)).setText(currentItem.getTitle());
        ((TextView) view.findViewById(R.id.tvDetailDesc)).setText(currentItem.getDescription());

        List<NewsItem> related = DataSource.getRelated(newsId, currentItem.getCategory());
        RelatedAdapter relatedAdapter = new RelatedAdapter(related, relatedItem -> {
            Bundle args = new Bundle();
            args.putInt("newsId", relatedItem.getId());
            NavHostFragment.findNavController(this).navigate(R.id.action_detail_to_detail, args);
        });
        RecyclerView rvRelated = view.findViewById(R.id.rvRelated);
        rvRelated.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRelated.setAdapter(relatedAdapter);

        btnBookmark = view.findViewById(R.id.btnBookmark);
        checkBookmarkState();

        btnBookmark.setOnClickListener(v -> toggleBookmark());

        view.findViewById(R.id.btnBack).setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    private void checkBookmarkState() {
        new Thread(() -> {
            List<Integer> ids = db.bookmarkDao().getAllIds();
            isBookmarked = ids.contains(currentItem.getId());
            requireActivity().runOnUiThread(() -> btnBookmark.setImageResource(isBookmarked ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline));
        }).start();
    }

    private void toggleBookmark() {
        Bookmark bookmark = new Bookmark(currentItem.getId(), currentItem.getTitle(), currentItem.getDescription(), currentItem.getImageRes(), currentItem.getCategory()
        );
        new Thread(() -> {
            if (isBookmarked) {
                db.bookmarkDao().delete(bookmark);
                isBookmarked = false;
            } else {
                db.bookmarkDao().insert(bookmark);
                isBookmarked = true;
            }
            requireActivity().runOnUiThread(() -> {
                btnBookmark.setImageResource(isBookmarked ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
                Toast.makeText(getContext(), isBookmarked ? "Bookmarked!" : "Bookmark removed", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}
