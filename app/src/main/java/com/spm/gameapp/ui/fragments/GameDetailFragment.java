package com.spm.gameapp.ui.fragments;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.spm.gameapp.R;
import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.db.model.SavedGame;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.ui.adapter.ImageAdapter;
import com.spm.gameapp.viewmodel.GameDetailViewModel;
import com.spm.gameapp.viewmodel.UiState;
import com.spm.gameapp.viewmodel.ViewModelFactory;

public class GameDetailFragment extends Fragment {

    private static final String TAG = "GameDetailFragment";
    private int gameId;
    private GameDetailViewModel viewModel;

    // Vistes del layout
    private ViewPager2 vpCarrousel;
    private TextView txtTitle, txtDescription, txtExceptional;
    private ProgressBar progressBar;
    private ImageView imgDetail;
    private MaterialButton btnAddMyGames;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recuperem l'ID que ens arriba del fragment anterior
        if (getArguments() != null) {
            gameId = getArguments().getInt("gameId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Vincular vistes
        initViews(view);

        // 2. Configurar ViewModel
        setupViewModel();

        //listeners
        setUpListeners();

        // 3. Observar els canvis d'estat
        observeViewModel();

        // 4. Llançar la càrrega inicial
        viewModel.loadGameDetail(gameId);
    }

    private void setUpListeners() {
        btnAddMyGames.setOnClickListener(v -> {
            // Obtenim el joc actual de l'estat del ViewModel
            UiState<Game> state = viewModel.getGameState().getValue();
            if (state != null && state.data != null) {
                Game game = state.data;

                // Convertim Game (API) a SavedGame (Entitat DB)
                SavedGame savedGame = new SavedGame(
                        game.getId(),
                        game.getName(),
                        game.getBackgroundImage()
                );

                // Crida al ViewModel
                viewModel.saveGame(savedGame);

                // Pista: Podries mostrar un Toast de confirmació
                Toast.makeText(requireContext(), "Afegit a My Games", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(View view) {
        vpCarrousel = view.findViewById(R.id.vp_carrousel);
        txtTitle = view.findViewById(R.id.txt_detail_title);
        txtDescription = view.findViewById(R.id.txt_detail_description);
        txtExceptional = view.findViewById(R.id.txt_exceptional);
        progressBar = view.findViewById(R.id.progress_detail);
        imgDetail = view.findViewById(R.id.img_detail);
        btnAddMyGames = view.findViewById(R.id.btnAddMyGames);
    }

    private void setupViewModel() {
        // Obtenim la instància de la base de dades per al repositori
        AppDatabase db = AppDatabase.getInstance(requireContext());
        GameRepository repository = new GameRepository(db.savedGameDao());

        // Usem la factoria que ja tenim definida
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(GameDetailViewModel.class);
    }

    private void observeViewModel() {
        viewModel.getGameState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;

            switch (state.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    if (state.data != null) {
                        bindData(state.data);
                    }
                    break;

                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Error carregant: ", state.error);
                    break;
            }
        });
    }

    private void bindData(Game game) {
        txtTitle.setText(game.getName());

        // RAWG envia la descripció sovint en HTML
        if (game.getDescription() != null) {
            txtDescription.setText(Html.fromHtml(game.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtDescription.setText("Data de llançament: " + game.getReleased());
        }

        // Ratings dinàmics (Exemple per a 'exceptional')
        if (game.getRatings() != null) {
            txtExceptional.setText("Exceptional: " + game.getRatingsCount());
        }

        // CONFIGURACIÓ DEL CARRUSEL
        if (game.getShortScreenshots() != null) {
            ImageAdapter adapter = new ImageAdapter(game.getShortScreenshots());
            vpCarrousel.setAdapter(adapter);
        }

        // Configura la imatge principal (img_detail) també
        Glide.with(this).load(game.getBackgroundImage()).into(imgDetail);
    }
}
