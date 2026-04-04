package com.spm.gameapp.ui.fragments;

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
    private int spinnerInitCount = 0;
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
        Log.d(TAG, "onCreateView: Inflant layout");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * S'executa un cop la vista ha estat creada. Inicialitza el ViewModel, els components
     * de la UI i configura els observadors i listeners.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Iniciant configuració del fragment");

        // 1. Inicialitzar ViewModel amb la Factory per permetre la injecció del Repositori
        try {
            // Dins de onViewCreated
            AppDatabase db = AppDatabase.getInstance(requireContext());
            GameRepository repository = new GameRepository(db.savedGameDao(), db.gameEntryDao());
            ViewModelFactory factory = new ViewModelFactory(repository);
            viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
            Log.d(TAG, "onViewCreated: ViewModel inicialitzat correctament");
        } catch (Exception e) {
            Log.e(TAG, "onViewCreated: Error inicialitzant el ViewModel", e);
        }

        // 2. Inicialitzar components de la UI (referències a vistes)
        setupUI(view);

        // 3. Configurar el RecyclerView (layout manager i adaptador)
        setupRecyclerView(view);

        // 4. Configurar Listeners per als filtres (Spinners)
        setupListeners();

        // 5. Observar canvis en l'estat de les dades al ViewModel
        observeViewModel();

        viewModel.loadGames(null, null); // Crida directa sense el 'if' inicial

        Log.d(TAG, "onViewCreated: Fragment preparat i observant el ViewModel.");
    }

    /**
     * Obté les referències de les vistes del layout i configura els adaptadors dels Spinners
     * amb els prefixos personalitzats ("Order by:" i "Platform:").
     */
    private void setupUI(View view) {
        Log.d(TAG, "setupUI: Configurant Spinners");
        spnrOrder = view.findViewById(R.id.spnr_order);
        spnrPlatform = view.findViewById(R.id.spnr_platform);
        rvGames = view.findViewById(R.id.rv_games);

        // Adapter personalitzat per a l'ordre per mostrar el prefix només quan està seleccionat
        ArrayAdapter<OrderValue> orderAdapter = new ArrayAdapter<OrderValue>(
                getContext(),
                android.R.layout.simple_spinner_item,
                OrderValue.values()
        ) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                String label = getItem(position) != null ? getItem(position).getLabel() : "";
                ((TextView) v).setText(getString(R.string.order_by_prefix, label));
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
        Log.d(TAG, "setupRecyclerView: Inicialitzant l'adaptador i el grid");
        int spanCount = getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        rvGames.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

        adapter = new GameAdapter();

        adapter.setOnGameClickListener(game -> {
            Log.d(TAG, "onGameClick: Navegant al detall del joc ID=" + game.getId());
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
        Log.d(TAG, "setupListeners: Configurant onItemSelectedListeners");
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Hi ha 2 spinners, ignorem les 2 primeres inicialitzacions automàtiques
                if (spinnerInitCount < 2) {
                    spinnerInitCount++;
                    Log.d(TAG, "onItemSelected: Ignorant inicialització automàtica (" + spinnerInitCount + "/2)");
                    return;
                }

                OrderValue order = (OrderValue) spnrOrder.getSelectedItem();
                PlatformValue platform = (PlatformValue) spnrPlatform.getSelectedItem();

                Log.d(TAG, "onItemSelected: Filtres canviats. Order=" + (order != null ? order.getValue() : "null") +
                        ", Platform=" + (platform != null ? platform.getValue() : "null"));

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
        Log.d(TAG, "observeViewModel: Començant a observar gamesState");
        viewModel.getGamesState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) {
                Log.w(TAG, "observeViewModel: L'estat rebut és null");
                return;
            }

            Log.d(TAG, "observeViewModel: Nou estat rebut: " + state.status);
            switch (state.status) {
                case LOADING:
                    Log.d(TAG, "observeViewModel: Mostrant indicador de càrrega");
                    break;
                case SUCCESS:
                    if (state.data != null) {
                        Log.d(TAG, "observeViewModel: Èxit. S'han rebut " + state.data.size() + " jocs");
                        adapter.setGames(state.data);
                    } else {
                        Log.w(TAG, "observeViewModel: Èxit però la llista de jocs és null");
                    }
                    break;
                case ERROR:
                    Log.e(TAG, "observeViewModel: Error detectat en l'estat", state.error);
                    if (state.error != null) {
                        Log.e(TAG, "observeViewModel: Missatge d'error: " + state.error.getMessage());
                    }
                    break;
            }
        });
    }
}