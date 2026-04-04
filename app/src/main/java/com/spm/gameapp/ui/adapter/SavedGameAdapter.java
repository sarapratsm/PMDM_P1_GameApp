package com.spm.gameapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(SavedGame game);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(SavedGame game);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setGames(List<SavedGame> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SavedGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Fix: Use saved_game_item instead of game_item to avoid NullPointerException on btnDelete
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_game_item, parent, false);
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

    public class SavedGameViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGame;
        TextView txtTitle;
        ImageButton btnDelete;

        public SavedGameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGame = itemView.findViewById(R.id.img_item_game);
            txtTitle = itemView.findViewById(R.id.txt_item_game_title);
            btnDelete = itemView.findViewById(R.id.btn_delete);

            // CLIC A LA TARGETA: Només per seleccionar, NO per esborrar
            itemView.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (onItemClickListener != null && pos != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(games.get(pos));
                }
            });

            // CLIC A LA PAPERERA: Només per esborrar
            if (btnDelete != null) {
                btnDelete.setOnClickListener(v -> {
                    int pos = getBindingAdapterPosition();
                    if (onDeleteClickListener != null && pos != RecyclerView.NO_POSITION) {
                        onDeleteClickListener.onDeleteClick(games.get(pos));
                    }
                });
            }
        }

        public void bind(SavedGame game) {
            txtTitle.setText(game.getTitle());
            Glide.with(itemView.getContext())
                    .load(game.getImgUrl())
                    .placeholder(R.drawable.gameapp)
                    .into(imgGame);
        }
    }
}
