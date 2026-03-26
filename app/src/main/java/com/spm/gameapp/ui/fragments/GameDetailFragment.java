package com.spm.gameapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.spm.gameapp.R;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.viewmodel.GameDetailViewModel;
import com.spm.gameapp.viewmodel.ViewModelFactory;

public class GameDetailFragment extends Fragment {

    private static final String TAG = "GameDetailFragment";
    private int gameId;
    private GameDetailViewModel viewModel;

    private ImageView imgDetail;
    private TextView txtTitle;
    private TextView txtDescription;
    private ProgressBar progressBar;

    public GameDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameId = getArguments().getInt("gameId");
            Log.d(TAG, "onCreate: Rebut joc amb ID: " + gameId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicialitzar vistes
        imgDetail = view.findViewById(R.id.img_detail);
        txtTitle = view.findViewById(R.id.txt_detail_title);
        txtDescription = view.findViewById(R.id.txt_detail_description);
        progressBar = view.findViewById(R.id.progress_detail);

        // 2. Inicialitzar ViewModel
        AppDatabase db = AppDatabase.getInstance(requireContext());
        GameRepository repository = new GameRepository(db.savedGameDao());
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(GameDetailViewModel.class);

        // 3. Observar estat del joc
        observeViewModel();

        // 4. Carregar dades
        viewModel.loadGameDetail(gameId);
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
                        txtTitle.setText(state.data.getName());
                        // RAWG API retorna descripció en HTML sovint
                        txtDescription.setText(state.data.getReleased()); 
                        
                        Glide.with(this)
                                .load(state.data.getBackgroundImage())
                                .placeholder(R.drawable.gameapp)
                                .into(imgDetail);
                    }
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "observeViewModel: Error carregant detall", state.error);
                    break;
            }
        });
    }
}
