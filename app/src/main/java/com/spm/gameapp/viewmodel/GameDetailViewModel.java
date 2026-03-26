package com.spm.gameapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.repository.GameRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameDetailViewModel extends ViewModel {

    private final GameRepository repository;
    private final MutableLiveData<UiState<Game>> gameState = new MutableLiveData<>();

    public GameDetailViewModel(GameRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<Game>> getGameState() {
        return gameState;
    }

    public void loadGameDetail(int gameId) {
        gameState.setValue(UiState.loading());
        repository.getGameDetail(gameId).enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gameState.setValue(UiState.success(response.body()));
                } else {
                    gameState.setValue(UiState.error(new Exception("Error: " + response.code())));
                }
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                gameState.setValue(UiState.error(t));
            }
        });
    }
}
