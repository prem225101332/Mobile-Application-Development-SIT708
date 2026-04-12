package com.example.istreamapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.istreamapp.R;
import com.example.istreamapp.database.PlaylistItem;
import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    public interface OnItemClickListener  {
        void onItemClick(PlaylistItem item);
    }
    public interface OnItemDeleteListener {
        void onItemDelete(PlaylistItem item);
    }

    private List<PlaylistItem> items = new ArrayList<>();
    private final OnItemClickListener  clickListener;
    private final OnItemDeleteListener deleteListener;

    public PlaylistAdapter(OnItemClickListener clickListener, OnItemDeleteListener deleteListener) {
        this.clickListener  = clickListener;
        this.deleteListener = deleteListener;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistItem item = items.get(position);

        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvUrl.setText(item.videoUrl);
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(item));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onItemDelete(item));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex, tvUrl;
        ImageButton btnDelete;

        ViewHolder(View view) {
            super(view);
            tvIndex   = view.findViewById(R.id.tvIndex);
            tvUrl     = view.findViewById(R.id.tvVideoUrl);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }
}