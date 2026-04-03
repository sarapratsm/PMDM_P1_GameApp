package com.spm.gameapp.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.spm.gameapp.api.GameApi;
import com.spm.gameapp.api.GameApiService;
import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.api.model.GameList;
import com.spm.gameapp.db.dao.GameEntryDao;
import com.spm.gameapp.db.dao.SavedGameDao;
import com.spm.gameapp.db.model.GameEntry;
import com.spm.gameapp.db.model.SavedGame;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;

/**
 * Repositori centralitzat de l'aplicació.
 * Gestiona la comunicació amb l'API externa (RAWG) i la base de dades local (Room).
 * Actua com a Font Única de Veritat (Single Source of Truth).
 */
public class GameRepository {
    private static final String TAG = "GameRepository";
    private final GameApiService apiService;
    private final SavedGameDao savedGameDao;
    private final GameEntryDao gameEntryDao;
    
    // Executor per a operacions en fil secundari per no bloquejar la UI
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    // Handler per retornar els resultats al fil principal
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GameRepository(SavedGameDao savedGameDao, GameEntryDao gameEntryDao) {
        this.apiService = GameApi.getClient().create(GameApiService.class);
        this.savedGameDao = savedGameDao;
        this.gameEntryDao = gameEntryDao;
    }

    // --- MÈTODES D'API (RETROFIT) ---

    /**
     * Recupera el llistat de jocs des de l'API de RAWG.
     */
    public Call<GameList> getGames(String ordering, String platforms) {
        String ord = (ordering == null || ordering.isEmpty()) ? null : ordering;
        String plat = (platforms == null || platforms.isEmpty()) ? null : platforms;
        return apiService.getGames(GameApi.API_KEY, 20, ord, plat);
    }

    /**
     * Recupera el detall d'un joc concret per ID.
     */
    public Call<Game> getGameDetail(int gameId) {
        return apiService.getGameDetail(gameId, GameApi.API_KEY);
    }

    // --- MÈTODES DE BASE DE DADES (ROOM) ---

    /**
     * Interfície genèrica per a les operacions de base de dades.
     */
    public interface RepositoryCallback<T> {
        void onLoaded(T data);
        void onError(Throwable t);
    }

    // --- GESTIÓ DE SAVED GAMES ---

    public void saveGame(SavedGame game, Runnable onComplete) {
        executor.execute(() -> {
            try {
                savedGameDao.insert(game);
                if (onComplete != null) mainHandler.post(onComplete);
                Log.d(TAG, "Joc desat correctament: " + game.getTitle());
            } catch (Exception e) {
                Log.e(TAG, "Error desant el joc", e);
            }
        });
    }

    public void deleteGame(SavedGame game, Runnable onComplete) {
        executor.execute(() -> {
            try {
                savedGameDao.delete(game);
                if (onComplete != null) mainHandler.post(onComplete);
                Log.d(TAG, "Joc eliminat: " + game.getTitle());
            } catch (Exception e) {
                Log.e(TAG, "Error eliminant el joc", e);
            }
        });
    }

    public void getAllSavedGames(RepositoryCallback<List<SavedGame>> callback) {
        executor.execute(() -> {
            try {
                List<SavedGame> games = savedGameDao.getAllSavedGames();
                mainHandler.post(() -> callback.onLoaded(games));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    public void isGameSaved(int gameId, RepositoryCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                SavedGame game = savedGameDao.getSavedGameById(gameId);
                mainHandler.post(() -> callback.onLoaded(game != null));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    // --- GESTIÓ DE GAME ENTRIES (BLOG) ---

    public void insertGameEntry(GameEntry entry, Runnable onComplete) {
        executor.execute(() -> {
            try {
                gameEntryDao.insert(entry);
                if (onComplete != null) mainHandler.post(onComplete);
                Log.d(TAG, "Entrada de blog afegida al joc ID: " + entry.getGameId());
            } catch (Exception e) {
                Log.e(TAG, "Error afegint entrada de blog", e);
            }
        });
    }

    public void getEntriesForGame(int gameId, RepositoryCallback<List<GameEntry>> callback) {
        executor.execute(() -> {
            try {
                List<GameEntry> entries = gameEntryDao.getGameEntriesByGameId(gameId);
                mainHandler.post(() -> callback.onLoaded(entries));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    public void deleteGameEntry(GameEntry entry, Runnable onComplete) {
        executor.execute(() -> {
            try {
                gameEntryDao.delete(entry);
                if (onComplete != null) mainHandler.post(onComplete);
            } catch (Exception e) {
                Log.e(TAG, "Error eliminant entrada de blog", e);
            }
        });
    }
}
