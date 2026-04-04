package com.spm.gameapp.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.spm.gameapp.R;
import com.spm.gameapp.db.model.GameEntry;
import com.spm.gameapp.db.model.SavedGame;
import com.spm.gameapp.ui.adapter.BlogAdapter;
import com.spm.gameapp.viewmodel.MySavedGamesViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyGamesBlogFragment extends Fragment {

    private MySavedGamesViewModel mViewModel;
    private ActivityResultLauncher<String> galleryLauncher;
    private Uri selectedImageUri;

    private ImageView imgPreview;
    private TextInputEditText etComment;
    private MaterialButton btnAddEntry;
    private TextView txtBlogTitle;
    private RecyclerView rvEntries;
    private BlogAdapter blogAdapter;

    public MyGamesBlogFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        Glide.with(this).load(uri).into(imgPreview);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_games_blog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgPreview = view.findViewById(R.id.img_entry_preview);
        etComment = view.findViewById(R.id.et_comment);
        btnAddEntry = view.findViewById(R.id.btn_add_entry);
        txtBlogTitle = view.findViewById(R.id.txt_blog_game_title);
        rvEntries = view.findViewById(R.id.rv_blog_entries);

        mViewModel = new ViewModelProvider(requireActivity()).get(MySavedGamesViewModel.class);

        setupRecyclerView();

        imgPreview.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        btnAddEntry.setOnClickListener(v -> saveNewEntry());

        // 1. Observe selected game to update the UI title
        mViewModel.getSelectedGame().observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                txtBlogTitle.setText(game.getTitle() + " Blog");
            } else {
                txtBlogTitle.setText("Selecciona un joc");
            }
        });

        // 2. Observe the entries list. The ViewModel handles the loading when a game is selected.
        mViewModel.getEntriesState().observe(getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                blogAdapter.setEntries(entries);
            } else {
                blogAdapter.setEntries(new java.util.ArrayList<>());
            }
        });
    }

    private void setupRecyclerView() {
        blogAdapter = new BlogAdapter();
        rvEntries.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEntries.setAdapter(blogAdapter);
    }

    private void saveNewEntry() {
        if (etComment.getText() == null) return;
        
        String comment = etComment.getText().toString().trim();
        SavedGame currentGame = mViewModel.getSelectedGame().getValue();

        if (currentGame != null && !comment.isEmpty()) {
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String imgUrl = (selectedImageUri != null) ? selectedImageUri.toString() : "";

            GameEntry entry = new GameEntry(currentGame.getGameId(), comment, date, imgUrl);

            mViewModel.insertGameEntry(entry, () -> {
                etComment.setText("");
                imgPreview.setImageResource(android.R.drawable.ic_menu_camera);
                selectedImageUri = null;
                Toast.makeText(getContext(), "Entrada afegida!", Toast.LENGTH_SHORT).show();
            });
        } else if (currentGame == null) {
            Toast.makeText(getContext(), "Selecciona un joc primer", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Escriu un comentari primer", Toast.LENGTH_SHORT).show();
        }
    }
}
