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
import com.spm.gameapp.api.model.ParentPlatform;
import com.spm.gameapp.api.model.Rating;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.db.model.SavedGame;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.ui.adapter.ImageAdapter;
import com.spm.gameapp.viewmodel.GameDetailViewModel;
import com.spm.gameapp.viewmodel.ViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GameDetailFragment extends Fragment {

    private static final String TAG = "GameDetailFragment";
    private int gameId;
    private GameDetailViewModel viewModel;

    // Vistes del layout
    private ViewPager2 vpCarrousel;
    private ImageAdapter imageAdapter = new ImageAdapter();
    private TextView txtDetailTitle, txtDescription, txtRatingCount, txtMajorRating;
    private TextView txtReleasedDate, txtPlaytime;
    private LinearLayout llPlatformIcons;
    private ProgressBar progressDetail;
    private ImageView imgDetail;
    private MaterialButton btnAddMyGames;
    private ChipGroup cgRatingsBars;
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
        
        // Càrrega de dades inicials
        viewModel.loadGameDetail(gameId);
        viewModel.loadScreenshots(gameId);
        viewModel.checkIfGameIsSaved(gameId);
    }

    private void initViews(View view) {
        txtDetailTitle = view.findViewById(R.id.txt_detail_title);
        txtDescription = view.findViewById(R.id.txt_detail_description);
        txtRatingCount = view.findViewById(R.id.txt_rating_count);
        txtReleasedDate = view.findViewById(R.id.txt_released_date);
        txtPlaytime = view.findViewById(R.id.txt_playtime);
        llPlatformIcons = view.findViewById(R.id.ll_platform_icons);
        progressDetail = view.findViewById(R.id.progress_detail);
        imgDetail = view.findViewById(R.id.img_detail);
        btnAddMyGames = view.findViewById(R.id.btnAddMyGames);
        cgRatingsBars = view.findViewById(R.id.cg_ratings_bars);
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
                    Toast.makeText(requireContext(), R.string.add_to_my_games, Toast.LENGTH_SHORT).show();
                    // Refresquem l'estat després de guardar
                    viewModel.checkIfGameIsSaved(gameId);
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

        // Nou observador per l'estat del botó
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
                btnAddMyGames.setIconResource(android.R.drawable.ic_input_add); // Icona original
                btnAddMyGames.setBackgroundColor(Color.WHITE);
                btnAddMyGames.setTextColor(Color.BLACK);
            }
        });
    }

    private void bindData(Game game) {
        if (txtDetailTitle != null) txtDetailTitle.setText(game.getName());

        // Released Date
        if (txtReleasedDate != null && game.getReleased() != null) {
            txtReleasedDate.setText(formatDate(game.getReleased()));
        }

        // Playtime
        if (txtPlaytime != null) {
            String playtimeText = "AVERAGE PLAYTIME: " + game.getPlaytime() + " HOURS";
            txtPlaytime.setText(playtimeText);
        }

        // Platform Icons
        if (llPlatformIcons != null && game.getParentPlatforms() != null) {
            setupPlatformIcons(game.getParentPlatforms());
        }

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
            
            if (!game.getRatings().isEmpty()) {
                String topRating = game.getRatings().get(0).getTitle();
                topRating = topRating.substring(0, 1).toUpperCase() + topRating.substring(1);

                if (txtMajorRating != null) {
                    txtMajorRating.setText(topRating + " 🎯");
                }
            }
        }
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fromApi = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat toDisplay = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            Date date = fromApi.parse(dateStr);
            return toDisplay.format(date).toUpperCase();
        } catch (ParseException e) {
            return dateStr;
        }
    }

    private void setupPlatformIcons(List<ParentPlatform> platforms) {
        llPlatformIcons.removeAllViews();
        for (ParentPlatform p : platforms) {
            ImageView icon = new ImageView(getContext());
            int iconRes = getPlatformIcon(p.getPlatform().getSlug());
            if (iconRes != 0) {
                icon.setImageResource(iconRes);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
                params.setMargins(0, 0, 12, 0);
                icon.setLayoutParams(params);
                icon.setColorFilter(Color.WHITE);
                llPlatformIcons.addView(icon);
            }
        }
    }

    private int getPlatformIcon(String slug) {
        switch (slug) {
            case "pc": return R.drawable.windows;
            case "playstation": return R.drawable.playstation;
            case "xbox": return R.drawable.xbox;
            case "ios": return R.drawable.ios;
            case "android": return R.drawable.android;
            case "mac": return R.drawable.mac;
            case "linux": return R.drawable.linux;
            case "nintendo": return R.drawable.nintendo;
            default: return 0;
        }
    }

    private void setupRatings(List<Rating> ratings, int totalRatings) {
        if (cgRatingsBars == null || !isAdded() || getContext() == null) return;
        cgRatingsBars.removeAllViews(); 

        if (txtRatingCount != null) {
            txtRatingCount.setText(totalRatings + " RATINGS");
        }

        for (Rating rating : ratings) {
            TextView tvRating = new TextView(requireContext());
            String title = rating.getTitle().substring(0, 1).toUpperCase() + rating.getTitle().substring(1);
            tvRating.setText(title + " " + rating.getCount());
            tvRating.setTextColor(Color.WHITE);
            tvRating.setPadding(0, 10, 0, 10);

            int color;
            switch (rating.getTitle().toLowerCase()) {
                case "exceptional": color = Color.GREEN; break;
                case "recommended": color = Color.BLUE; break;
                case "meh": color = Color.YELLOW; break;
                case "skip": color = Color.RED; break;
                default: color = Color.GRAY;
            }

            tvRating.setCompoundDrawablesWithIntrinsicBounds(createCircle(color), null, null, null);
            tvRating.setCompoundDrawablePadding(20);
            cgRatingsBars.addView(tvRating);
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
