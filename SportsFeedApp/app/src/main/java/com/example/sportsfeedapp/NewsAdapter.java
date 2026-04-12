// adapter/NewsAdapter.java
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
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    private List<NewsItem> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public NewsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<NewsItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
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
            image    = view.findViewById(R.id.ivNews);
            title    = view.findViewById(R.id.tvNewsTitle);
            category = view.findViewById(R.id.tvNewsCategory);
        }
    }
}
