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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spm.gameapp.R;
import com.spm.gameapp.core.enums.*;
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
        
        // 1. Inicialitzar ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // 2. Inicialitzar components de la UI
        setupUI(view);
        
        // 3. Configurar el RecyclerView
        setupRecyclerView();

        // 4. Listeners per als filtres (Spinners)
        setupListeners();
        
        // 5. Observar dades del ViewModel
        observeViewModel();
        
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

    private void setupRecyclerView() {
        int spanCount = getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        rvGames.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        
        adapter = new GameAdapter();
        rvGames.setAdapter(adapter);
    }

    private void setupListeners() {
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrderValue order = (OrderValue) spnrOrder.getSelectedItem();
                PlatformValue platform = (PlatformValue) spnrPlatform.getSelectedItem();

                // Avisem al ViewModel que carregui amb els nous filtres
                viewModel.loadGames(order.getApiValue(), platform.getApiValue());
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

            switch (state.getStatus()) {
                case LOADING:
                    // TODO: Mostrar ProgressBar
                    Log.d(TAG, "observeViewModel: Carregant jocs...");
                    break;
                case SUCCESS:
                    // TODO: Amagar ProgressBar
                    if (state.getData() != null) {
                        adapter.setGames(state.getData());
                    }
                    break;
                case ERROR:
                    // TODO: Amagar ProgressBar i mostrar error (Toast/Snackbar)
                    Log.e(TAG, "observeViewModel: Error carregant jocs", state.getError());
                    break;
            }
        });
    }
}