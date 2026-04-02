package com.spm.gameapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.spm.gameapp.repository.GameRepository;

/**
 * Factory per a crear instàncies de ViewModel.
 * */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final GameRepository repository;

    public ViewModelFactory(GameRepository repository) {
        this.repository = repository;
    }

    /**
     * Crea instàncies de ViewModel.
     * @param <T> Tipus de ViewModel a crear.
     * @param modelClass Classe del ViewModel a crear.
     */
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        } else if (modelClass.isAssignableFrom(GameDetailViewModel.class)) {
            return (T) new GameDetailViewModel(repository);
        }else if (modelClass.isAssignableFrom(MySavedGamesViewModel.class)) {
            return (T) new MySavedGamesViewModel(repository);
        }
        // Podem afegir altres ViewModels aquí si comparteixen el repositori
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
