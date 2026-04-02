package com.spm.gameapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.api.model.GameList;
import com.spm.gameapp.repository.GameRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel per a la pantalla d'inici (HomeFragment).
 * Funció: Cridar a l'API de RAWG per obtenir la llista de jocs.
 * Responsabilitats: Gestionar l'estat de la llista (càrrega, error, èxit), aplicar l'ordre
 * (per exemple, per "Average rating") i filtrar per plataforma (PC, PS4, etc.).
 */
public class HomeViewModel extends ViewModel {

    private final GameRepository repository;
    // LiveData per a l'estat de càrrega i resultats
    private final MutableLiveData<UiState<List<Game>>> gamesState = new MutableLiveData<>();

    public HomeViewModel(GameRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<Game>>> getGamesState() {
        return gamesState;
    }

    public void loadGames(String ordering, String platforms) {
        gamesState.setValue(UiState.loading());
        repository.getGames(ordering, platforms).enqueue(new Callback<GameList>() {
            @Override
            public void onResponse(Call<GameList> call, Response<GameList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gamesState.setValue(UiState.success(response.body().getResults()));
                } else {
                    gamesState.setValue(UiState.error(new Exception("Error: " + response.code())));
                }
            }

            @Override
            public void onFailure(Call<GameList> call, Throwable t) {
                gamesState.setValue(UiState.error(t));
            }
        });
    }
}
