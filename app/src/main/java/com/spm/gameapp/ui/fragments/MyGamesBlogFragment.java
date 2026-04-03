package com.spm.gameapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spm.gameapp.R;
import com.spm.gameapp.viewmodel.MySavedGamesViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyGamesBlogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGamesBlogFragment extends Fragment {

    private static final String TAG = "BlogFragment";
    private int gameId;
    //Utilitza el mateix viewModel que GameDetailViewModel
    private MySavedGamesViewModel mViewModel;


    public MyGamesBlogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyGamesBlogFragment newInstance(String param1, String param2) {
        MyGamesBlogFragment fragment = new MyGamesBlogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_games_blog, container, false);

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(MySavedGamesViewModel.class);
        mViewModel = new ViewModelProvider(requireActivity()).get(MySavedGamesViewModel.class);

        TextView txtGameTitle = view.findViewById(R.id.txt_blog_game_title);
        mViewModel.getSelectedGame().observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                txtGameTitle.setText(game.getTitle() + " Blog");
                //Carregar entrades del blog
                //TODO
                loadBlogEntries();
            }
        });

        //configurar botó d'afegir entrada
        view.findViewById(R.id.btn_add_entry).setOnClickListener(v -> {
            saveNewEntry();
        });

    }

    /**
     * Mètode per a carregar les entrades del blog des de la Room.
     */
    private void loadBlogEntries() {



    }

    /**
     * Mètode per a guardar una nova entrada al blog.
     */
    private void saveNewEntry() {

    }


}


