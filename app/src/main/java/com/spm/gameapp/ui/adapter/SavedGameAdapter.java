package com.spm.gameapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spm.gameapp.R;
import com.spm.gameapp.db.model.SavedGame;

import java.util.ArrayList;
import java.util.List;

public class SavedGameAdapter extends RecyclerView.Adapter<SavedGameAdapter.SavedGameViewHolder> {

    private List<SavedGame> games = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SavedGame game);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setGames(List<SavedGame> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SavedGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        return new SavedGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedGameViewHolder holder, int position) {
        holder.bind(games.get(position));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class SavedGameViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGame;
        TextView txtTitle;
        LinearLayout llPlatforms;

        public SavedGameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGame = itemView.findViewById(R.id.img_game);
            txtTitle = itemView.findViewById(R.id.txt_title);
            llPlatforms = itemView.findViewById(R.id.ll_platforms);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(games.get(pos));
                }
            });
        }

        public void bind(SavedGame game) {
            //bind data to the views
            txtTitle.setText(game.getTitle());
            Glide.with(itemView.getContext()).load(game.getImgUrl()).into(imgGame);
            llPlatforms.removeAllViews();


        }
    }
}
