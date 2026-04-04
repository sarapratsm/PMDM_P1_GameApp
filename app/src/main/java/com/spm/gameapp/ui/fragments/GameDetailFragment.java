package com.spm.gameapp.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.spm.gameapp.R;
import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.api.model.Genre;
import com.spm.gameapp.api.model.Rating;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.db.model.SavedGame;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.ui.adapter.ImageAdapter;
import com.spm.gameapp.viewmodel.GameDetailViewModel;
import com.spm.gameapp.viewmodel.ViewModelFactory;

import java.util.List;

public class GameDetailFragment extends Fragment {

    private static final String TAG = "GameDetailFragment";
    private int gameId;
    private GameDetailViewModel viewModel;

    // Vistes del layout
    private ViewPager2 vpCarrousel;
    private ImageAdapter imageAdapter = new ImageAdapter();
    private TextView txtDetailTitle, txtDescription, txtRatingCount, txtMajorRating;
    private ProgressBar progressDetail;
    private ImageView imgDetail;
    private MaterialButton btnAddMyGames;
    private ChipGroup llRatingsBars;
    private ChipGroup chipGroupGenres;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameId = getArguments().getInt("gameId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupViewModel();
        setUpListeners();
        observeViewModel();
        viewModel.loadGameDetail(gameId);
        viewModel.loadScreenshots(gameId);
        viewModel.checkIfGameIsSaved(gameId);
    }

    private void initViews(View view) {
        txtDetailTitle = view.findViewById(R.id.txt_detail_title);
        txtDescription = view.findViewById(R.id.txt_detail_description);
        txtRatingCount = view.findViewById(R.id.txt_rating_count);
        progressDetail = view.findViewById(R.id.progress_detail);
        imgDetail = view.findViewById(R.id.img_detail);
        btnAddMyGames = view.findViewById(R.id.btnAddMyGames);
        llRatingsBars = view.findViewById(R.id.cg_ratings_bars);
        chipGroupGenres = view.findViewById(R.id.chip_group_genres);
        vpCarrousel = view.findViewById(R.id.vp_carrousel);
        txtMajorRating = view.findViewById(R.id.txt_major_rating);
        
        if (vpCarrousel != null) {
            vpCarrousel.setAdapter(imageAdapter);
            vpCarrousel.setOffscreenPageLimit(1);
        }
    }

    private void setupViewModel() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        GameRepository repository = new GameRepository(db.savedGameDao(), db.gameEntryDao());
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(GameDetailViewModel.class);
    }

    private void setUpListeners() {
        if (btnAddMyGames != null) {
            btnAddMyGames.setOnClickListener(v -> {
                if (viewModel.getGameState().getValue() != null && viewModel.getGameState().getValue().data != null) {
                    Game game = viewModel.getGameState().getValue().data;
                    SavedGame savedGame = new SavedGame(game.getId(), game.getName(), game.getBackgroundImage());
                    viewModel.saveGame(savedGame);
                    viewModel.checkIfGameIsSaved(game.getId());
                    Toast.makeText(requireContext(), R.string.add_to_my_games, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void observeViewModel() {
        viewModel.getGameState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            switch (state.status) {
                case LOADING:
                    if (progressDetail != null) progressDetail.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    if (progressDetail != null) progressDetail.setVisibility(View.GONE);
                    if (state.data != null) bindData(state.data);
                    break;
                case ERROR:
                    if (progressDetail != null) progressDetail.setVisibility(View.GONE);
                    Log.e(TAG, "Error carregant detall: ", state.error);
                    break;
            }
        });

        viewModel.getScreenshots().observe(getViewLifecycleOwner(), screenshots -> {
            if (screenshots != null) {
                imageAdapter.setScreenshots(screenshots);
            }
        });

        //observador per al botó
        viewModel.getIsSaved().observe(getViewLifecycleOwner(), isSaved -> {
            if (isSaved) {
                btnAddMyGames.setText("In my collection");
                btnAddMyGames.setEnabled(false); // Opcional: el desactivem
                btnAddMyGames.setIconResource(android.R.drawable.checkbox_on_background);
                btnAddMyGames.setBackgroundColor(Color.parseColor("#2E7D32")); // Verd fosc
                btnAddMyGames.setTextColor(Color.WHITE);
            } else {
                btnAddMyGames.setText(R.string.add_to_my_games);
                btnAddMyGames.setEnabled(true);
                btnAddMyGames.setBackgroundColor(Color.WHITE);
                btnAddMyGames.setTextColor(Color.BLACK);
            }
        });
    }

    private void bindData(Game game) {
        if (txtDetailTitle != null) txtDetailTitle.setText(game.getName());

        if (game.getDescription() != null && txtDescription != null) {
            txtDescription.setText(Html.fromHtml(game.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else if (game.getDescriptionRaw() != null && txtDescription != null) {
            txtDescription.setText(game.getDescriptionRaw());
        }

        if (imgDetail != null) {
            Glide.with(this).load(game.getBackgroundImage()).into(imgDetail);
        }

        if (chipGroupGenres != null) {
            chipGroupGenres.removeAllViews();
            if (game.getGenres() != null) {
                for (Genre genre : game.getGenres()) {
                    Chip chip = new Chip(getContext());
                    chip.setText(genre.getName());
                    chipGroupGenres.addView(chip);
                }
            }
        }

        if (game.getRatings() != null) {
            setupRatings(game.getRatings(), game.getRatingsCount());
            
            // Agafem la primera valoració (sol ser la majoritària)
            if (!game.getRatings().isEmpty()) {
                String topRating = game.getRatings().get(0).getTitle();
                topRating = topRating.substring(0, 1).toUpperCase() + topRating.substring(1);

                if (txtMajorRating != null) {
                    txtMajorRating.setText(topRating);
                }
            }
        }
    }

    private void setupRatings(List<Rating> ratings, int totalRatings) {
        // 1. Verificació de seguretat: ens assegurem que el contenidor existeix
        if (llRatingsBars == null || !isAdded() || getContext() == null) return;

        llRatingsBars.removeAllViews();

        if (txtRatingCount != null) {
            txtRatingCount.setText(totalRatings + " RATINGS");
        }

        for (Rating rating : ratings) {
            // 2. Creem un Chip en lloc d'un TextView
            Chip chip = new Chip(requireContext());

            String title = rating.getTitle().substring(0, 1).toUpperCase() + rating.getTitle().substring(1);
            chip.setText(title + " " + rating.getCount());

            // Estil del Chip per a mode fosc
            chip.setTextColor(Color.WHITE);
            chip.setChipBackgroundColorResource(android.R.color.transparent);
            chip.setChipStrokeColorResource(android.R.color.darker_gray);
            chip.setChipStrokeWidth(2f);

            // 3. Assignem el color del cercle
            int color;
            switch (rating.getTitle().toLowerCase()) {
                case "exceptional": color = Color.GREEN; break;
                case "recommended": color = Color.BLUE; break;
                case "meh": color = Color.YELLOW; break;
                case "skip": color = Color.RED; break;
                default: color = Color.GRAY;
            }

            // 4. Posem el cercle com a "Chip Icon" (part esquerra)
            chip.setChipIcon(createCircle(color));
            chip.setChipIconVisible(true);
            chip.setIconStartPadding(10f);

            // Afegim el chip al ChipGroup
            llRatingsBars.addView(chip);
        }
    }

    private ShapeDrawable createCircle(int color) {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(24);
        circle.setIntrinsicWidth(24);
        circle.getPaint().setColor(color);
        return circle;
    }
}
