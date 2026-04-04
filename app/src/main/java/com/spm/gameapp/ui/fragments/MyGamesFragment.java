package com.spm.gameapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.spm.gameapp.R;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.viewmodel.MySavedGamesViewModel;
import com.spm.gameapp.viewmodel.ViewModelFactory;

/**
 * Fragment amfitrió de la secció My Games.
 * Gestiona la disposició de la llista (Master) i el blog (Detail).
 * També s'encarrega de les accions globals com l'exportació de dades.
 */
public class MyGamesFragment extends Fragment {

    private MySavedGamesViewModel viewModel;
    private ImageButton btnShare;

    public MyGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicialitzar el ViewModel compartit a nivell d'Activity.
        // Això permet que MyGamesListFragment i MyGamesBlogFragment comparteixin dades.
        AppDatabase db = AppDatabase.getInstance(requireContext());
        GameRepository repository = new GameRepository(db.savedGameDao(), db.gameEntryDao());
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MySavedGamesViewModel.class);

        // 2. Vincular vistes globals
        btnShare = view.findViewById(R.id.btn_share);

        // 3. Configurar accions globals
        btnShare.setOnClickListener(v -> {
            // TODO: Implementar l'exportació a Excel tal com demana l'AGENTS.md
            Toast.makeText(requireContext(), "Preparant exportació a Excel...", Toast.LENGTH_SHORT).show();
        });

        // 4. Disparar la càrrega de jocs guardats si encara no s'ha fet
        if (viewModel.getGamesState().getValue() == null) {
            viewModel.loadGames();
        }
    }
}
