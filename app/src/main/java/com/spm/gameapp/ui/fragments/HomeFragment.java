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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spm.gameapp.R;
import com.spm.gameapp.utils.enums.*;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.ui.adapter.GameAdapter;
import com.spm.gameapp.viewmodel.*;

/**
 * Fragment principal de l'aplicació que mostra el llistat de jocs obtinguts de l'API de RAWG.
 * Inclou funcionalitats de filtratge per plataforma i ordenació dels resultats.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    
    // Components de la interfície d'usuari
    private Spinner spnrOrder;
    private Spinner spnrPlatform;
    private RecyclerView rvGames;
    private GameAdapter adapter;

    // Lògica de negoci i dades
    private HomeViewModel viewModel;

    /**
     * Constructor buit obligatori per a fragments.
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Infla la vista del fragment a partir del recurs XML.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * S'executa un cop la vista ha estat creada. Inicialitza el ViewModel, els components
     * de la UI i configura els observadors i listeners.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 1. Inicialitzar ViewModel amb la Factory per permetre la injecció del Repositori
        AppDatabase db = AppDatabase.getInstance(requireContext());
        GameRepository repository = new GameRepository(db.savedGameDao());
        ViewModelFactory factory = new ViewModelFactory(repository);
        
        // requireActivity() s'usa si volem que el ViewModel es comparteixi a nivell d'Activity,
        // altrament s'usaria 'this' per un ViewModel propi del Fragment.
        viewModel = new ViewModelProvider(requireActivity(), factory).get(HomeViewModel.class);

        // 2. Inicialitzar components de la UI (referències a vistes)
        setupUI(view);

        // 3. Configurar el RecyclerView (layout manager i adaptador)
        setupRecyclerView(view);

        // 4. Configurar Listeners per als filtres (Spinners)
        setupListeners();

        // 5. Observar canvis en l'estat de les dades al ViewModel
        observeViewModel();
        
        // Realitzar una càrrega inicial si l'estat actual és buit
        if (viewModel.getGamesState().getValue() == null) {
            viewModel.loadGames(null, null);
        }

        Log.d(TAG, "onViewCreated: Fragment preparat i observant el ViewModel.");
    }

    /**
     * Obté les referències de les vistes del layout i configura els adaptadors dels Spinners
     * amb els prefixos personalitzats ("Order by:" i "Platform:").
     */
    private void setupUI(View view) {
        spnrOrder = view.findViewById(R.id.spnr_order);
        spnrPlatform = view.findViewById(R.id.spnr_platform);
        rvGames = view.findViewById(R.id.rv_games);

        // Adapter personalitzat per a l'ordre per mostrar el prefix només quan està seleccionat
        ArrayAdapter<OrderValue> orderAdapter = new ArrayAdapter<OrderValue>(
                getContext(),
                android.R.layout.simple_spinner_item,
                OrderValue.values()
        ) {
            //getView ens permet modificar la vista de l'element seleccionat
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // Fem servir el recurs de string amb placeholder per evitar hardcoding
                String label = getItem(position) != null ? getItem(position).getLabel() : "";
                ((TextView) v).setText(getString(R.string.order_by_prefix, label));
                return v;
            }

            // getDropDownView ens permet modificar la vista del menú desplegable
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (getItem(position) != null) {
                    ((TextView) v).setText(getItem(position).getLabel());
                }
                return v;
            }
        };
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrOrder.setAdapter(orderAdapter);

        // Adapter personalitzat per a la plataforma per mostrar el prefix només quan està seleccionat
        ArrayAdapter<PlatformValue> platformAdapter = new ArrayAdapter<PlatformValue>(
                getContext(),
                android.R.layout.simple_spinner_item,
                PlatformValue.values()
        ) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                String label = getItem(position) != null ? getItem(position).getLabel() : "";
                ((TextView) v).setText(getString(R.string.platform_prefix, label));
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (getItem(position) != null) {
                    ((TextView) v).setText(getItem(position).getLabel());
                }
                return v;
            }
        };
        platformAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrPlatform.setAdapter(platformAdapter);
    }

    /**
     * Configura el RecyclerView amb un GridLayoutManager dinàmic segons l'orientació
     * i inicialitza l'adaptador de jocs gestionant el click per navegar al detall.
     */
    private void setupRecyclerView(View view) {
        // Canviem el nombre de columnes segons l'orientació (2 en vertical, 3 en horitzontal)
        int spanCount = getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        rvGames.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        
        adapter = new GameAdapter();

        // Configuració del callback quan es prem un joc: naveguem al detall passant l'ID
        adapter.setOnGameClickListener(game -> {
            Bundle args = new Bundle();
            args.putInt("gameId", game.getId());
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_gameDetailFragment, args);
        });
        
        rvGames.setAdapter(adapter);
    }

    /**
     * Configura els listeners dels Spinners per detectar quan l'usuari canvia un filtre
     * i demana al ViewModel que torni a carregar les dades de l'API.
     */
    private void setupListeners() {
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrderValue order = (OrderValue) spnrOrder.getSelectedItem();
                PlatformValue platform = (PlatformValue) spnrPlatform.getSelectedItem();

                // Demanem al ViewModel carregar jocs amb els nous filtres
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

    /**
     * Observa el LiveData de l'estat dels jocs al ViewModel per reaccionar a les actualitzacions:
     * mostra l'estat de càrrega, els resultats obtinguts o els errors produïts.
     */
    private void observeViewModel() {
        viewModel.getGamesState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;

            switch (state.status) {
                case LOADING:
                    // TODO: Gestionar visualment l'estat de càrrega (ex: mostrar ProgressBar)
                    Log.d(TAG, "observeViewModel: Carregant jocs...");
                    break;
                case SUCCESS:
                    // Si la càrrega és correcta, passem la llista de jocs a l'adaptador
                    if (state.data != null) {
                        adapter.setGames(state.data);
                    }
                    break;
                case ERROR:
                    // TODO: Mostrar un missatge d'error a l'usuari (Toast o Snackbar)
                    Log.e(TAG, "observeViewModel: Error carregant jocs", state.error);
                    break;
            }
        });
    }
}
