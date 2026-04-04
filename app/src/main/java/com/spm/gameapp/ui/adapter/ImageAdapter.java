package com.spm.gameapp.ui.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.spm.gameapp.R;
import com.spm.gameapp.api.model.Screenshot;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Screenshot> screenshots = new ArrayList<>();

    public void setScreenshots(List<Screenshot> screenshots) {
        this.screenshots = screenshots;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creem la vista amb els LayoutParams específics de RecyclerView per evitar que el carrusel col·lapsi
        ImageView imageView = new ImageView(parent.getContext());
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (screenshots == null || screenshots.isEmpty()) return;
        
        String imageUrl = screenshots.get(position).getImage();
        Glide.with(holder.imageView.getContext())
                .load(imageUrl)
                .override(800, 600) // Limitem la mida en memòria
                .placeholder(R.drawable.gameapp)
                .error(R.drawable.gameapp)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return screenshots != null ? screenshots.size() : 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView;
        }
    }
}
