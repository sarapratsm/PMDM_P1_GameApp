package com.spm.gameapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spm.gameapp.db.model.SavedGame;
import com.spm.gameapp.repository.GameRepository;

import java.util.List;

/**
 * Per gestionar la persistència amb Room (afegir/eliminar preferits i el blog).
 * Funció: Recuperar els jocs desats a la base de dades local.
 * Responsabilitats: Gestionar la persistència dels comentaris tipus blog i
 * les imatges de la galeria, i preparar les dades per ser compartides en format Excel (csv).
 */
public class MySavedGamesViewModel  extends ViewModel {
    private static final String TAG = "MySavedGamesViewModel";

    // Repositori per a accedir a la base de dades
    private final GameRepository repository;
    // LiveData per a gestionar l'estat de les dades

    // 2. L'estat de la pantalla (Llista de jocs desats)
    private final MutableLiveData<UiState<List<SavedGame>>> gamesState = new MutableLiveData<>();
    private final MutableLiveData<SavedGame> selectedGame = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showBlog = new MutableLiveData<>();

    // Constructor
    public MySavedGamesViewModel(GameRepository repository) {
        this.repository = repository;
    }

    // Getter per a l'estat de les dades
    public MutableLiveData<UiState<List<SavedGame>>> getGamesState() {
        return gamesState;
    }
    public MutableLiveData<Boolean> getShowBlog() {
        return showBlog;
    }
    public MutableLiveData<SavedGame> getSelectedGame() {
        return selectedGame;
    }

    // Funcions per a gestionar l'estat de les dades
    public void selectGame(SavedGame game) {
        selectedGame.setValue(game);
    }

    public void deleteGame(SavedGame game, Runnable onComplete) {
        repository.deleteGame(game, onComplete);
    }

    public void closeBlog() {
        showBlog.setValue(false);
    }


    // Funció per a carregar els jocs desats
    public void loadGames() {
        gamesState.setValue(UiState.loading());
        //demanar dades al repository
        repository.getAllSavedGames(new GameRepository.RepositoryCallback<List<SavedGame>>() {


            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: " + t.getMessage());
                gamesState.setValue(UiState.error(t));

            }
            @Override
            public void onLoaded(List<SavedGame> games) {
                gamesState.setValue(UiState.success(games));
                Log.d(TAG, "onLoaded: " + games.size());
            }
        });
    }


}
