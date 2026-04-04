package com.spm.gameapp.ui.adapter;

import android.util.Log;
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
import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.api.model.ParentPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private static final String TAG = "GameAdapter";
    private List<Game> games = new ArrayList<>();
    private OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    public GameAdapter() {
    }

    public void setOnGameClickListener(OnGameClickListener listener) {
        this.listener = listener;
    }

    public void setGames(List<Game> games) {
        this.games = games;
        notifyDataSetChanged();
        Log.d(TAG, "setGames: List updated with " + games.size() + " games");
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.bind(game);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgGame;
        private final TextView txtTitle;
        private final TextView txtRating;
        private final LinearLayout llPlatforms;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGame = itemView.findViewById(R.id.img_game);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtRating = itemView.findViewById(R.id.txt_rating);
            llPlatforms = itemView.findViewById(R.id.ll_platforms);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onGameClick(games.get(position));
                }
            });
        }

        public void bind(Game game) {
            txtTitle.setText(game.getName());
            
            if (game.getRating() != null) {
                txtRating.setText(String.format(Locale.getDefault(), "%.1f", game.getRating()));
                txtRating.setVisibility(View.VISIBLE);
            } else {
                txtRating.setVisibility(View.GONE);
            }

            // Carregar imatge amb Glide
            Glide.with(itemView.getContext())
                    .load(game.getBackgroundImage())
                    .placeholder(R.drawable.gameapp)
                    .error(R.drawable.gameapp)
                    .centerCrop()
                    .into(imgGame);

            // Gestionar icones de plataformes
            llPlatforms.removeAllViews();
            if (game.getParentPlatforms() != null) {
                for (ParentPlatform pp : game.getParentPlatforms()) {
                    ImageView icon = new ImageView(itemView.getContext());
                    int iconRes = getPlatformIcon(pp.getPlatform().getSlug());
                    if (iconRes != 0) {
                        icon.setImageResource(iconRes);
                        
                        // Ajustar mida de la icona (més petita i estilitzada)
                        int size = (int) (16 * itemView.getContext().getResources().getDisplayMetrics().density);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                        params.setMargins(0, 0, 12, 0);
                        icon.setLayoutParams(params);
                        icon.setAlpha(0.7f); // Subtilment transparent
                        
                        llPlatforms.addView(icon);
                    }
                }
            }
        }

        private int getPlatformIcon(String slug) {
            switch (slug) {
                case "pc": return R.drawable.windows;
                case "playstation": return R.drawable.playstation;
                case "xbox": return R.drawable.xbox;
                case "nintendo": return R.drawable.nintendo;
                case "ios": return R.drawable.ios;
                case "android": return R.drawable.android;
                case "linux": return R.drawable.linux;
                case "mac": return R.drawable.mac;
                default:
                    return R.drawable.mygames;

            }
        }
    }
}
