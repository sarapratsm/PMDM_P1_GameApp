package com.spm.gameapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.db.model.SavedGame;
import com.spm.gameapp.repository.GameRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 *Funció: Obtenir la informació detallada d'un joc específic
 * (sovint una altra crida a l'API) i gestionar la interacció
 * de l'usuari amb la pantalla de detall del joc.
 * Responsabilitats: Controlar el carrusel d'imatges
 * i fer el pont per desar el joc a la base de dades local (Room)
 * quan l'usuari prem el botó "+".*/
public class GameDetailViewModel extends ViewModel {

    private final GameRepository repository;
    // LiveData que el Fragment observarà
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

    public void saveGame(SavedGame savedGame) {
        repository.saveGame(savedGame, () -> {
            //Runnable buit
        });
    }
}
