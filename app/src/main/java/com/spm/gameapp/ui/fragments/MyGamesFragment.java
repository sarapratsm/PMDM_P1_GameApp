package com.spm.gameapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.spm.gameapp.R;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.viewmodel.MySavedGamesViewModel;
import com.spm.gameapp.viewmodel.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGamesFragment extends Fragment {

    private static final String TAG = "MyGamesFragment";

    // Components de la interfície d'usuari (titol, botó...)
    private TextView txtMyGames;
    private ImageButton btnShare;
    private RecyclerView rvMyGames;


    // Lògica de negoci i dades
    private MySavedGamesViewModel viewModel;

    /**
     * Constructor buit obligatori per a fragments.
     */
    public MyGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicialitzar ViewModel amb la Factory per permetre la injecció del Repositori
        AppDatabase db = AppDatabase.getInstance(requireContext());
        GameRepository repository = new GameRepository(db.savedGameDao(), db.gameEntryDao());
        ViewModelFactory factory = new ViewModelFactory(repository);

        // Corregit: Cal passar la factory per crear el ViewModel que té un constructor amb paràmetres
        viewModel = new ViewModelProvider(this, factory).get(MySavedGamesViewModel.class);

        //2. Inicialitzar components de la UI (referències a vistes)
        txtMyGames = view.findViewById(R.id.txt_mygames_title);
        btnShare = view.findViewById(R.id.btn_share);
        //list_container
        rvMyGames = view.findViewById(R.id.list_container);
        //blog_container
        rvMyGames = view.findViewById(R.id.blog_container);


        //3. Observar canvis en l'estat de les dades al ViewModel
        observeViewModel();

        //4. Realitzar una càrrega inicial si l'estat actual és buit
        if (viewModel.getGamesState().getValue() == null) {
            viewModel.loadGames();
        }
    }

    private void observeViewModel() {
        viewModel.getGamesState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
        });
        //TODO: Mostrar els jocs desats
        //TODO: Mostrar el blog



    }
}
