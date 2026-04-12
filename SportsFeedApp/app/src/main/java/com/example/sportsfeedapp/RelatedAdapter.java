package com.example.sportsfeedapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sportsfeedapp.R;
import java.util.List;

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    private final List<NewsItem> items;
    private final OnItemClickListener listener;

    public RelatedAdapter(List<NewsItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem item = items.get(position);
        
        Glide.with(holder.itemView.getContext()).load(item.getImageRes()).into(holder.image);
                
        holder.title.setText(item.getTitle());
        holder.category.setText(item.getCategory());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, category;

        ViewHolder(View view) {
            super(view);
            image    = view.findViewById(R.id.ivRelated);
            title    = view.findViewById(R.id.tvRelatedTitle);
            category = view.findViewById(R.id.tvRelatedCategory);
        }
    }
}
