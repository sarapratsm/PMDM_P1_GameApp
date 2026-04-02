package com.spm.gameapp.ui.adapter;

import static java.lang.System.load;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spm.gameapp.R;
import com.spm.gameapp.api.model.ShortScreenshot;

import java.util.List;

/**
 * Adaptador per a mostrar una llista d'imatges.
 * Utilitza la llibreria Glide per a carregar les imatges.*/
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final List<ShortScreenshot> screenshots;

    public ImageAdapter(List<ShortScreenshot> screenshots) {
        this.screenshots = screenshots;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new ViewHolder
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        String imageUrl = screenshots.get(position).getImage();
        Glide.with(holder.imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.gameapp)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return screenshots != null ? screenshots.size() : 0;    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }
    }
}
