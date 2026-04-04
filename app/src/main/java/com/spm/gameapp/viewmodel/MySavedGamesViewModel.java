package com.spm.gameapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spm.gameapp.db.model.GameEntry;
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

    private final GameRepository repository;

    private final MutableLiveData<UiState<List<SavedGame>>> gamesState = new MutableLiveData<>();
    private final MutableLiveData<SavedGame> selectedGame = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showBlog = new MutableLiveData<>();
    
    // LiveData per a les entrades del blog del joc seleccionat
    private final MutableLiveData<List<GameEntry>> entriesState = new MutableLiveData<>();

    public MySavedGamesViewModel(GameRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<SavedGame>>> getGamesState() {
        return gamesState;
    }
    
    public LiveData<Boolean> getShowBlog() {
        return showBlog;
    }
    
    public LiveData<SavedGame> getSelectedGame() {
        return selectedGame;
    }

    public LiveData<List<GameEntry>> getEntriesState() {
        return entriesState;
    }

    public void selectGame(SavedGame game) {
        selectedGame.setValue(game);
        if (game != null) {
            loadEntriesForGame(game.getGameId());
        } else {
            entriesState.setValue(null);
        }
    }

    public void deleteGame(SavedGame game, Runnable onComplete) {
        repository.deleteGame(game, onComplete);
    }

    public void closeBlog() {
        showBlog.setValue(false);
    }

    public void loadGames() {
        gamesState.setValue(UiState.loading());
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

    public void loadEntriesForGame(int gameId) {
        repository.getEntriesForGame(gameId, new GameRepository.RepositoryCallback<List<GameEntry>>() {
            @Override
            public void onLoaded(List<GameEntry> entries) {
                entriesState.setValue(entries);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error loading blog entries", t);
            }
        });
    }

    public void insertGameEntry(GameEntry entry, Runnable onComplete) {
        repository.insertGameEntry(entry, () -> {
            loadEntriesForGame(entry.getGameId());
            if (onComplete != null) onComplete.run();
        });
    }
    public String generateCsvData() {
        List<GameEntry> currentEntries = entriesState.getValue();
        SavedGame currentGame = selectedGame.getValue();

        if (currentGame == null || currentEntries == null || currentEntries.isEmpty()) {
            return null; // Res per exportar
        }

        StringBuilder csv = new StringBuilder();
        // Capçalera del CSV
        csv.append("Game,Date,Comment\n");

        // Dades
        for (GameEntry entry : currentEntries) {
            String safeComment = entry.getComment() != null ? entry.getComment().replace("\"", "\"\"") : "";
            csv.append("\"").append(currentGame.getTitle()).append("\",")
                    .append("\"").append(entry.getDate()).append("\",")
                    .append("\"").append(safeComment).append("\"\n");
        }

        return csv.toString();
    }
}
