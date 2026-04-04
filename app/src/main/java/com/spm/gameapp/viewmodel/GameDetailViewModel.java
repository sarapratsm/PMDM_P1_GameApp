package com.spm.gameapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.api.model.Screenshot;
import com.spm.gameapp.db.model.SavedGame;
import com.spm.gameapp.repository.GameRepository;

import java.util.List;

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
    private static final String TAG = "GameDetailViewModel";

    private final GameRepository repository;
    // LiveData que el Fragment observarà
    private final MutableLiveData<UiState<Game>> gameState = new MutableLiveData<>();
    private final MutableLiveData<List<Screenshot>> screenshots = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSaved = new MutableLiveData<>();
    public GameDetailViewModel(GameRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<Game>> getGameState() {
        return gameState;
    }
    public LiveData<List<Screenshot>> getScreenshots() {
        return screenshots;
    }
    public LiveData<Boolean> getIsSaved() {
        return isSaved;
    }

    public void loadGameDetail(int gameId) {
        Log.d(TAG, "loadGameDetail: carregant detall del joc ID=" + gameId);
        gameState.setValue(UiState.loading());
        repository.getGameDetail(gameId).enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "loadGameDetail: Detall del joc carregat correctament");
                    gameState.setValue(UiState.success(response.body()));
                } else {
                    Log.e(TAG, "loadGameDetail: Error en la resposta. Codi: " + response.code());
                    gameState.setValue(UiState.error(new Exception("Error: " + response.code())));
                }
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                Log.e(TAG, "loadGameDetail: Error de xarxa", t);
                gameState.setValue(UiState.error(t));
            }
        });
    }

    public void saveGame(SavedGame savedGame) {
        Log.d(TAG, "saveGame: demanant desar el joc " + savedGame.getTitle());
        repository.saveGame(savedGame, () -> {
            Log.d(TAG, "saveGame: Joc desat correctament");
        });
    }

    public void loadScreenshots(int gameId) {
        Log.d(TAG, "loadScreenshots: carregant captures del joc ID=" + gameId);
        repository.getGameScreenshots(gameId, new GameRepository.RepositoryCallback<List<Screenshot>>() {
            @Override
            public void onLoaded(List<Screenshot> data) {
                Log.d(TAG, "loadScreenshots: Captures carregades correctament");
                screenshots.setValue(data);
            }
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "loadScreenshots: Error carregant captures", t);
            }
        });
    }

    public void checkIfGameIsSaved(int gameId) {
        repository.isGameSaved(gameId, new GameRepository.RepositoryCallback<Boolean>() {
            @Override
            public void onLoaded(Boolean data) {
                // Actualitzem el LiveData amb la resposta (true/false)
                isSaved.setValue(data);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error comprovant si el joc està desat", t);
                isSaved.setValue(false);
            }
        });
    }
}
