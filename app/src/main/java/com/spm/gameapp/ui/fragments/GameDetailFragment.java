package com.spm.gameapp.ui.fragments;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.spm.gameapp.R;
import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.viewmodel.GameDetailViewModel;
import com.spm.gameapp.viewmodel.ViewModelFactory;

public class GameDetailFragment extends Fragment {

    private static final String TAG = "GameDetailFragment";
    private int gameId;
    private GameDetailViewModel viewModel;

    // Vistes del layout
    private ViewPager2 vpCarrousel;
    private TextView txtTitle, txtDescription, txtExceptional;
    private ProgressBar progressBar;

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

        // 3. Observar els canvis d'estat
        observeViewModel();

        // 4. Llançar la càrrega inicial
        viewModel.loadGameDetail(gameId);
    }

    private void initViews(View view) {
        vpCarrousel = view.findViewById(R.id.vp_carrousel);
        txtTitle = view.findViewById(R.id.txt_detail_title);
        txtDescription = view.findViewById(R.id.txt_detail_description);
        txtExceptional = view.findViewById(R.id.txt_exceptional);
        progressBar = view.findViewById(R.id.progress_detail);
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
                    // Aquí podries mostrar un Toast o un missatge d'error a l'usuari
                    break;
            }
        });
    }

    private void bindData(Game game) {
        txtTitle.setText(game.getName());

        // Tractament de la descripció amb HTML
        if (game.getSlug() != null) { // RAWG sovint posa la descripció detallada aquí o en un camp 'description'
            // Si el model Game té 'description', fes servir:
            // txtDescription.setText(Html.fromHtml(game.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            txtDescription.setText("Data de llançament: " + game.getReleased());
        }

        // Exemple de puntuació (Page 4 del PDF: Exceptional, Recommended, etc.)
        if (game.getRatings() != null && !game.getRatings().isEmpty()) {
            // Podries buscar el rating "exceptional" a la llista
            txtExceptional.setText("Exceptional: " + game.getRatingsCount());
        }

        // CONFIGURACIÓ DEL CARRUSEL
        // Perquè el ViewPager2 funcioni, necessites un adaptador (ImageAdapter)
        // setupCarousel(game.getShortScreenshots());
    }
}