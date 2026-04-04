package com.spm.gameapp.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.spm.gameapp.R;
import com.spm.gameapp.db.AppDatabase;
import com.spm.gameapp.repository.GameRepository;
import com.spm.gameapp.viewmodel.MySavedGamesViewModel;
import com.spm.gameapp.viewmodel.ViewModelFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
            String csvData = viewModel.generateCsvData();
            Toast.makeText(requireContext(), "Preparant exportació a Excel...", Toast.LENGTH_SHORT).show();

            if (csvData == null) {
                Toast.makeText(requireContext(), "No hi ha res a exportar (selecciona un joc amb entrades)", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // 1. Crear un fitxer temporal a la cache de l'app
                File exportDir = new File(requireContext().getCacheDir(), "exports");
                if (!exportDir.exists()) exportDir.mkdirs();

                File file = new File(exportDir, "blog_export.csv");
                FileWriter writer = new FileWriter(file);
                writer.append(csvData);
                writer.flush();
                writer.close();

                // 2. Obtenir l'Uri segura usant FileProvider
                // ALERTA: Assegura't que "com.spm.gameapp.fileprovider" coincideix amb l'authorities del teu AndroidManifest.xml
                Uri uri = FileProvider.getUriForFile(requireContext(), "com.spm.gameapp.fileprovider", file);

                // 3. Crear i llançar l'Intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/csv");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Exportació Blog Joc");
                // Donem permís temporal a l'app de destí per llegir el fitxer
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(shareIntent, "Compartir via..."));

            } catch (IOException e) {
                Log.e("MyGamesFragment", "Error escrivint el fitxer CSV", e);
                Toast.makeText(requireContext(), "Error en generar l'exportació", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                Log.e("MyGamesFragment", "Error amb el FileProvider. Revisa el Manifest.", e);
                Toast.makeText(requireContext(), "Error de configuració (FileProvider)", Toast.LENGTH_SHORT).show();
            }
        });
        
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
                    listContainer.setVisibility(View.VISIBLE);
                    //Si hi ha un joc seleccionat, es veu el blog
                    if(viewModel.getSelectedGame() != null){
                        blogContainer.setVisibility(View.VISIBLE);
                    }else{
                        blogContainer.setVisibility(View.GONE);
                    }

                }
            }
        });
    }
}
