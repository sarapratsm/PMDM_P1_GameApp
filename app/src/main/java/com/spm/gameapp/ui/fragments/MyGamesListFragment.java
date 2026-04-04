package com.spm.gameapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spm.gameapp.R;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.ui.adapter.SavedGameAdapter;
import com.spm.gameapp.viewmodel.MySavedGamesViewModel;
import com.spm.gameapp.viewmodel.ViewModelFactory;

public class MyGamesListFragment extends Fragment {

    private MySavedGamesViewModel viewModel;
    private RecyclerView rvMyGames;
    private SavedGameAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_games_list, container, false);
        adapter = new SavedGameAdapter();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMyGames = view.findViewById(R.id.rv_my_games_list);
        
        // 1. IMPORTANT: Usem 'requireActivity()' perquè el ViewModel sigui compartit amb el fragment del Blog
        AppDatabase db = AppDatabase.getInstance(requireContext());
        GameRepository repository = new GameRepository(db.savedGameDao(), db.gameEntryDao());
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MySavedGamesViewModel.class);

        setupRecyclerView();

        adapter.setOnDeleteClickListener(game -> {
            // 1. Demanem al ViewModel que esborri el joc de Room
            viewModel.deleteGame(game, () -> {
                /* 2. Un cop esborrat, recarreguem la llista per actualitzar la UI */
                viewModel.loadGames();
                Toast.makeText(requireContext(), game.getTitle() + " eliminat", Toast.LENGTH_SHORT).show();
            });
        });

        observeViewModel();
    }

    private void setupRecyclerView() {
        //defineix una quadrícula de dues columnes
        rvMyGames.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new SavedGameAdapter();
        
        // Quan cliquem un joc, el seleccionem al ViewModel
        adapter.setOnItemClickListener(game -> {
            viewModel.selectGame(game);
        });

        rvMyGames.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getGamesState().observe(getViewLifecycleOwner(), state -> {
            if (state != null && state.data != null) {
                adapter.setGames(state.data);
            }
        });
    }
}
