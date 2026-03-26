package com.spm.gameapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.spm.gameapp.repository.GameRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final GameRepository repository;

    public ViewModelFactory(GameRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        }
        // Pots afegir altres ViewModels aquí si comparteixen el repositori
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
