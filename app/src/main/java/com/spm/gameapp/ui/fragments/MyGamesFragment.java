package com.spm.gameapp.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
    private View listContainer;
    private View blogContainer;


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
        listContainer = view.findViewById(R.id.list_container);
        blogContainer = view.findViewById(R.id.blog_container);

        // 3. Configurar accions globals
        btnShare.setOnClickListener(v -> {
            // TODO: Implementar l'exportació a Excel tal com demana l'AGENTS.md
            Toast.makeText(requireContext(), "Preparant exportació a Excel...", Toast.LENGTH_SHORT).show();
        });

        // 4. Disparar la càrrega de jocs guardats si encara no s'ha fet
        if (viewModel.getGamesState().getValue() == null) {
            viewModel.loadGames();
        }
        
        configOnBackPressed();
        observeViewModel();
    }

    private void configOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Si estem en vertical i el blog és visible, el tanquem
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                                && blogContainer.getVisibility() == View.VISIBLE) {

                            blogContainer.setVisibility(View.GONE);
                            listContainer.setVisibility(View.VISIBLE);
                            viewModel.selectGame(null); // Deseleccionem el joc
                        } else {
                            // Si no, fem el "back" normal (sortir de la pantalla)
                            setEnabled(false);
                            requireActivity().onBackPressed();
                        }
                    }
                });
    }

    // Lògica de gestió de la visibilitat del blog:
    private void observeViewModel() {
        viewModel.getSelectedGame().observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                // Comprovem l'orientació actual
                int orientation = getResources().getConfiguration().orientation;

                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // EN VERTICAL: Fem la transició (amaguem llista, mostrem blog)
                    listContainer.setVisibility(View.GONE);
                    blogContainer.setVisibility(View.VISIBLE);
                } else {
                    // EN HORITZONTAL: Ja es veuen els dos, només ens assegurem
                    listContainer.setVisibility(View.VISIBLE);
                    blogContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
