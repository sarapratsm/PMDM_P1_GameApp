package com.spm.gameapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spm.gameapp.R;
import com.spm.gameapp.db.model.GameEntry;

import java.util.ArrayList;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private List<GameEntry> entries = new ArrayList<>();

    public void setEntries(List<GameEntry> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog_entry, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        holder.bind(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        ImageView imgScreenshot;
        TextView txtDate;
        TextView txtComment;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            imgScreenshot = itemView.findViewById(R.id.img_blog_screenshot);
            txtDate = itemView.findViewById(R.id.txt_blog_date);
            txtComment = itemView.findViewById(R.id.txt_blog_comment);
        }

        public void bind(GameEntry entry) {
            txtDate.setText(entry.getDate());
            txtComment.setText(entry.getComment());

            // Correct: Use getImgUrl() as defined in GameEntry
            if (entry.getImgUrl() != null && !entry.getImgUrl().isEmpty()) {
                imgScreenshot.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(entry.getImgUrl())
                        .placeholder(R.drawable.gameapp)
                        .into(imgScreenshot);
            } else {
                imgScreenshot.setVisibility(View.GONE);
            }
        }
    }
}
