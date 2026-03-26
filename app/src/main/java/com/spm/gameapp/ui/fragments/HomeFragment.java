package com.spm.gameapp.ui.fragments;

import static com.spm.gameapp.viewmodel.UiState.Status.LOADING;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spm.gameapp.R;
import com.spm.gameapp.core.enums.*;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.ui.adapter.GameAdapter;
import com.spm.gameapp.viewmodel.*;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    
    private Spinner spnrOrder;
    private Spinner spnrPlatform;
    private RecyclerView rvGames;
    private GameAdapter adapter;
    private HomeViewModel viewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 1. Inicialitzar ViewModel amb Factory
        AppDatabase db = AppDatabase.getInstance(requireContext());
        GameRepository repository = new GameRepository(db.savedGameDao());
        ViewModelFactory factory = new ViewModelFactory(repository);
        
        viewModel = new ViewModelProvider(requireActivity(), factory).get(HomeViewModel.class);

        // 2. Inicialitzar components de la UI
        setupUI(view);
        
        // 3. Configurar el RecyclerView
        setupRecyclerView(view);

        // 4. Listeners per als filtres (Spinners)
        setupListeners();
        
        // 5. Observar dades del ViewModel
        observeViewModel();
        
        // Càrrega inicial si és necessari
        if (viewModel.getGamesState().getValue() == null) {
            viewModel.loadGames(null, null);
        }
        
        Log.d(TAG, "onViewCreated: Fragment preparat i observant el ViewModel.");
    }

    private void setupUI(View view) {
        spnrOrder = view.findViewById(R.id.spnr_order);
        spnrPlatform = view.findViewById(R.id.spnr_platform);
        rvGames = view.findViewById(R.id.rv_games);

        // Configurar adapters per als Spinners usant els Enums
        spnrOrder.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, OrderValue.values()));
        spnrPlatform.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, PlatformValue.values()));
    }

    private void setupRecyclerView(View view) {
        int spanCount = getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        rvGames.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        
        adapter = new GameAdapter();
        
        // Configurar el click per navegar al detall
        adapter.setOnGameClickListener(game -> {
            Bundle args = new Bundle();
            args.putInt("gameId", game.getId());
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_gameDetailFragment, args);
        });
        
        rvGames.setAdapter(adapter);
    }

    private void setupListeners() {
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrderValue order = (OrderValue) spnrOrder.getSelectedItem();
                PlatformValue platform = (PlatformValue) spnrPlatform.getSelectedItem();

                // Avisem al ViewModel que carregui amb els nous filtres
                if (order != null && platform != null) {
                    viewModel.loadGames(order.getValue(), platform.getValue());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spnrOrder.setOnItemSelectedListener(filterListener);
        spnrPlatform.setOnItemSelectedListener(filterListener);
    }

    private void observeViewModel() {
        viewModel.getGamesState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;

            switch (state.status) {
                case LOADING:
                    Log.d(TAG, "observeViewModel: Carregant jocs...");
                    break;
                case SUCCESS:
                    if (state.data != null) {
                        adapter.setGames(state.data);
                    }
                    break;
                case ERROR:
                    Log.e(TAG, "observeViewModel: Error carregant jocs", state.error);
                    break;
            }
        });
    }
}
